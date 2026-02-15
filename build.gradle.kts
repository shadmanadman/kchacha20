plugins {
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}