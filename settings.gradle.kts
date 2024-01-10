rootProject.name = "Roots"

pluginManagement {
    repositories {
        maven {
            url = uri("https://nexus.gtnewhorizons.com/repository/public/")
        }
        gradlePluginPortal()
        mavenCentral()
//        mavenLocal()
    }
}

plugins {
    // Automatic toolchain provisioning
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.4.0"
}