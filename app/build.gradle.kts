import java.net.URI
import groovy.json.JsonSlurper

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.cyclonedx)
}

val apiBaseUrl = providers.gradleProperty("pactogg.apiBaseUrl").orElse("https://52-3-200-211.sslip.io/").get()
val privacyVersion = providers.gradleProperty("pactogg.privacyVersion").orElse("2026-10-01").get()
require(privacyVersion.matches(Regex("[A-Za-z0-9_.-]{1,32}"))) { "Versión de privacidad inválida." }
require(apiBaseUrl.isEmpty() || URI(apiBaseUrl).let {
    it.scheme == "https" && it.host != null && it.userInfo == null &&
        it.query == null && it.fragment == null && it.path.endsWith("/")
}) { "pactogg.apiBaseUrl debe ser una URL HTTPS pública, sin credenciales y terminada en /." }


// Client configuration only: service-account credentials are never accepted here.
val googleConfig = file("google-services.json").takeIf { it.exists() }?.let {
    JsonSlurper().parse(it) as Map<*, *>
}
require(googleConfig?.get("type") != "service_account") { "Usa la configuracion Android, no una cuenta de servicio." }
val googleAndroidClient = (googleConfig?.get("client") as? List<*>)?.filterIsInstance<Map<*, *>>()?.singleOrNull {
    val info = it["client_info"] as? Map<*, *>
    val androidInfo = info?.get("android_client_info") as? Map<*, *>
    androidInfo?.get("package_name") == "com.pactogg.app"
}
require(googleConfig == null || googleAndroidClient != null) { "El archivo Firebase no corresponde a com.pactogg.app." }
val configuredWebClient = (googleAndroidClient?.get("oauth_client") as? List<*>)
    ?.filterIsInstance<Map<*, *>>()?.firstOrNull { it["client_type"].toString() == "3" }
    ?.get("client_id") as? String ?: ""
val googleWebClientId = providers.gradleProperty("pactogg.googleWebClientId").orElse(configuredWebClient).get()
require(googleWebClientId.isEmpty() || googleWebClientId.matches(
    Regex("[A-Za-z0-9._-]+\\.apps\\.googleusercontent\\.com"))) { "Client ID web de Google invalido." }
android {
    namespace = "com.pactogg.app"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.pactogg.app"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"
        // Public Android client resources; never embed server service-account credentials.
        val project = googleConfig?.get("project_info") as? Map<*, *>
        val info = googleAndroidClient?.get("client_info") as? Map<*, *>
        val keys = googleAndroidClient?.get("api_key") as? List<*>
        val key = keys?.filterIsInstance<Map<*, *>>()?.firstOrNull()?.get("current_key") as? String
        mapOf("google_app_id" to info?.get("mobilesdk_app_id"),
            "gcm_defaultSenderId" to project?.get("project_number"),
            "project_id" to project?.get("project_id"), "google_api_key" to key).forEach { (name, value) ->
            val text = value?.toString().orEmpty()
            require(text.matches(Regex("[A-Za-z0-9_:.-]*"))) { "Configuracion Firebase Android invalida." }
            resValue("string", name, text)
        }
        buildConfigField("String", "API_BASE_URL", "\"$apiBaseUrl\"")
        buildConfigField("String", "PRIVACY_VERSION", "\"$privacyVersion\"")
        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"$googleWebClientId\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testCoverage { jacocoVersion = "0.8.14" }
    buildTypes {
        debug { enableUnitTestCoverage = true }
        release {
            optimization {
                enable = true
            }
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
        resValues = true
    }
    lint {
        abortOnError = true
        warningsAsErrors = true
        checkReleaseBuilds = true
        // Version updates are reviewed explicitly; do not make this gate depend on newest-version hints.
        disable += setOf("GradleDependency", "NewerVersionAvailable", "AndroidGradlePluginVersion", "OldTargetApi")
    }
}

ksp { arg("room.schemaLocation", "$projectDir/schemas") }

dependencies {
    implementation(libs.firebase.messaging)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services)
    implementation(libs.google.identity)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.graphics.path)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.retrofit)
    implementation(libs.retrofit.serialization)
    implementation(libs.okhttp)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.androidx.exifinterface)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.okhttp.tls)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.room.testing)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}

val detektTool by configurations.creating
dependencies {
 detektTool(libs.detekt.cli) }
tasks.register<JavaExec>("detekt") {
    group = "verification"
    description = "Checks Kotlin sources using the pinned standalone Detekt CLI."
    classpath = detektTool
    mainClass.set("io.gitlab.arturbosch.detekt.cli.Main")
    args("--input", "src/main/java", "--config", "../config/detekt.yml", "--build-upon-default-config",
        "--report", "xml:build/reports/detekt/detekt.xml")
}

group = "com.pactogg"
version = "0.1.0"
tasks.named<org.cyclonedx.gradle.CyclonedxDirectTask>("cyclonedxDirectBom") {
    includeConfigs = listOf("releaseRuntimeClasspath")
}

// Separate CI categories reuse the same JUnit4 tests. No test-engine migration.
val testSuite = providers.gradleProperty("pactogg.testSuite").orElse("all").get()
require(testSuite in listOf("all", "unit", "integration", "security")) { "Unknown pactogg.testSuite" }
tasks.withType<Test>().configureEach {
    useJUnit {
        if (testSuite != "all") includeCategories("com.pactogg.app.testing." + when (testSuite) {
            "unit" -> "UnitChecks"
            "integration" -> "IntegrationChecks"
            else -> "SecurityChecks"
        })
    }
}
