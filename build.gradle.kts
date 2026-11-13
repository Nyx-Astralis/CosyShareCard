plugins {
    alias(libs.plugins.android.library)
    id("maven-publish")
    id("com.vanniktech.maven.publish") version "0.37.0"
}

android {
    namespace = "com.studio868.cosysharecard"

    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.keep")
    }

    buildFeatures {
        viewBinding = true
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}

base {
    archivesName.set("CosyShareCard")
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates(
        "io.github.nyx-astralis",
        "cosysharecard",
        "1.0.0"
    )

    pom {
        name.set("CosyShareCard")
        description.set("An Android library for creating and sharing cozy customizable image share cards.")
        inceptionYear.set("2026")
        url.set("https://github.com/Nyx-Astralis/CosyShareCard")

        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
                distribution.set("repo")
            }
        }

        developers {
            developer {
                id.set("nyx-astralis")
                name.set("Nyx Astralis")
                url.set("https://github.com/Nyx-Astralis")
            }
        }

        scm {
            url.set("https://github.com/Nyx-Astralis/CosyShareCard")
            connection.set("scm:git:git://github.com/Nyx-Astralis/CosyShareCard.git")
            developerConnection.set("scm:git:ssh://git@github.com/Nyx-Astralis/CosyShareCard.git")
        }
    }
}




afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                from(components["release"])

                groupId = "com.github.Nyx-Astralis"
                artifactId = "CosyShareCard"
                version = System.getenv("VERSION") ?: "1.0.0"
            }
        }
    }
}

tasks.configureEach {
    if (name == "signReleasePublication" ||
        name == "publishReleasePublicationToMavenCentralRepository") {
        enabled = false
    }
}