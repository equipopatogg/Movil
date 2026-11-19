import groovy.json.JsonSlurper
import java.net.URI

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
// Aviso completo publicado en la página del equipo (requisito 16: vincular el aviso a la landing).
val privacyUrl = providers.gradleProperty("pactogg.privacyUrl")
    .orElse("https://black-river-065cb1710.6.azurestaticapps.net/privacidad").get()
require(privacyUrl.startsWith("https://") && privacyUrl.none(Char::isWhitespace)) { "URL del aviso inválida." }
// Client ID web de OAuth (público): es el "aud" que valida el backend en POST auth/google. No es un secreto.
val googleWebClientId = providers.gradleProperty("pactogg.googleWebClientId")
    .orElse("841778632932-0jnd246u1mvbl6n9djtthsdgvb0um6do.apps.googleusercontent.com").get()
require(googleWebClientId.isEmpty() || googleWebClientId.matches(
    Regex("[A-Za-z0-9._-]+\\.apps\\.googleusercontent\\.com"))) { "Client ID web de Google inválido." }
require(privacyVersion.matches(Regex("[A-Za-z0-9_.-]{1,32}"))) { "Versión de privacidad inválida." }
require(apiBaseUrl.isEmpty() || URI(apiBaseUrl).let {
    it.scheme == "https" && it.host != null && it.userInfo == null &&
        it.query == null && it.fragment == null && it.path.endsWith("/")
}) { "pactogg.apiBaseUrl debe ser una URL HTTPS pública, sin credenciales y terminada en /." }

// PL-10: solo la parte pública del cliente Android de Firebase; nunca una cuenta de servicio.
// google-services.json no se versiona: sin él la app compila y el push queda "no configurado".
val firebaseConfig = file("google-services.json").takeIf { it.exists() }?.let { JsonSlurper().parse(it) as Map<*, *> }
require(firebaseConfig?.get("type") != "service_account") { "Usa la configuración Android, no una cuenta de servicio." }
val firebaseClient = (firebaseConfig?.get("client") as? List<*>)?.filterIsInstance<Map<*, *>>()?.singleOrNull {
    ((it["client_info"] as? Map<*, *>)?.get("android_client_info") as? Map<*, *>)?.get("package_name") ==
        "com.pactogg.app"
}
require(firebaseConfig == null || firebaseClient != null) { "google-services.json no corresponde a com.pactogg.app." }
val firebaseValues = mapOf(
    "google_app_id" to (firebaseClient?.get("client_info") as? Map<*, *>)?.get("mobilesdk_app_id"),
    "gcm_defaultSenderId" to (firebaseConfig?.get("project_info") as? Map<*, *>)?.get("project_number"),
    "project_id" to (firebaseConfig?.get("project_info") as? Map<*, *>)?.get("project_id"),
    "google_api_key" to ((firebaseClient?.get("api_key") as? List<*>)?.firstOrNull() as? Map<*, *>)?.get("current_key"),
).mapValues { (_, value) -> value?.toString().orEmpty() }
require(firebaseValues.values.all { it.matches(Regex("[A-Za-z0-9_:.-]*")) }) { "Configuración Firebase inválida." }

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
        buildConfigField("String", "API_BASE_URL", "\"$apiBaseUrl\"")
        buildConfigField("String", "PRIVACY_VERSION", "\"$privacyVersion\"")
        buildConfigField("String", "PRIVACY_URL", "\"$privacyUrl\"")
        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"$googleWebClientId\"")
        firebaseValues.forEach { (name, value) -> resValue("string", name, value) }

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
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services)
    implementation(libs.google.identity)
    implementation(libs.firebase.messaging)
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
dependencies { detektTool(libs.detekt.cli) }
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
