plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.vanniktechPublish)
    id("dev.gobley.cargo") version "0.3.7"
    id("dev.gobley.uniffi") version "0.3.7"
    kotlin("plugin.atomicfu") version libs.versions.kotlin
    id("signing")
}

kotlin {
    cargo {
        packageDirectory = layout.projectDirectory.dir("../core")
    }
    androidTarget {
        publishLibraryVariants("release")
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "kchacha20"
            isStatic = true
        }
    }
}

android {
    namespace = providers.gradleProperty("ANDROID_NAMESPACE").get()
    compileSdk = libs.versions.android.compileSdk.get().toInt()
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    val tag = System.getenv("GITHUB_REF")?.substringAfterLast("/") ?: libs.versions.snapshotVersion.get()

    coordinates(
        groupId = libs.versions.groupId.get(),
        artifactId = libs.versions.artifactId.get(),
        version = tag
    )

    pom {
        name.set(providers.gradleProperty("POM_NAME").get())
        description.set(providers.gradleProperty("POM_DESCRIPTION").get())
        url.set(providers.gradleProperty("POM_URL").get())
        licenses {
            license {
                name.set(providers.gradleProperty("POM_LICENSE_NAME").get())
                url.set(providers.gradleProperty("POM_LICENSE_URL").get())
            }
        }
        developers {
            developer {
                id.set(providers.gradleProperty("POM_DEVELOPER_ID").get())
                name.set(providers.gradleProperty("POM_DEVELOPER_NAME").get())
                email.set(providers.gradleProperty("POM_DEVELOPER_EMAIL").get())
            }
        }
        scm {
            connection.set(providers.gradleProperty("SCM_CONNECTION").get())
            developerConnection.set(providers.gradleProperty("SCM_DEVELOPER_CONNECTION").get())
            url.set(providers.gradleProperty("POM_URL").get())
        }
    }
}

signing {
    val keyId = System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKeyId")
    val key = System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKey")
    val keyPassword = System.getenv("ORG_GRADLE_PROJECT_signingInMemoryKeyPassword")

    useInMemoryPgpKeys(
        keyId,
        key,
        keyPassword
    )
}
