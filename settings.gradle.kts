pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        // Espejo oficial de Maven Central hospedado por Google: responde bien a los runners de GitHub,
        // que Maven Central a veces limita por IP. Si algo no está aquí, Gradle sigue con Maven Central.
        maven("https://maven-central.storage-download.googleapis.com/maven2/") { name = "MavenCentralMirror" }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        // Espejo oficial de Maven Central hospedado por Google: responde bien a los runners de GitHub,
        // que Maven Central a veces limita por IP. Si algo no está aquí, Gradle sigue con Maven Central.
        maven("https://maven-central.storage-download.googleapis.com/maven2/") { name = "MavenCentralMirror" }
        mavenCentral()
    }
}

rootProject.name = "PactoGG"
include(":app")
