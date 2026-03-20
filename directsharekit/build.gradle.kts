plugins {
    alias(libs.plugins.android.library)
    `maven-publish`
}

group = (findProperty("POM_GROUP_ID") as String?) ?: "io.github.DesaiVinubhai"
version = (findProperty("POM_VERSION") as String?) ?: "1.0.0"

android {
    namespace = "com.csiw.directsharekit"
    compileSdk = 36

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }

    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.18.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = project.group.toString()
            artifactId = (findProperty("POM_ARTIFACT_ID") as String?) ?: "directsharekit"
            version = project.version.toString()

            afterEvaluate {
                from(components["release"])
            }

            pom {
                name.set((findProperty("POM_NAME") as String?) ?: "DirectShareKit")
                description.set(
                    (findProperty("POM_DESCRIPTION") as String?)
                        ?: "Reusable Android document sharing library"
                )
                url.set((findProperty("POM_URL") as String?) ?: "https://github.com/DesaiVinubhai/DirectShare")

                licenses {
                    license {
                        name.set((findProperty("POM_LICENSE_NAME") as String?) ?: "MIT License")
                        url.set((findProperty("POM_LICENSE_URL") as String?) ?: "https://opensource.org/license/mit/")
                    }
                }

                developers {
                    developer {
                        id.set((findProperty("POM_DEVELOPER_ID") as String?) ?: "DesaiVinubhai")
                        name.set((findProperty("POM_DEVELOPER_NAME") as String?) ?: "DesaiVinubhai")
                    }
                }

                scm {
                    connection.set((findProperty("POM_SCM_CONNECTION") as String?) ?: "scm:git:git://github.com/DesaiVinubhai/DirectShare.git")
                    developerConnection.set((findProperty("POM_SCM_DEV_CONNECTION") as String?) ?: "scm:git:ssh://github.com:DesaiVinubhai/DirectShare.git")
                    url.set((findProperty("POM_SCM_URL") as String?) ?: "https://github.com/DesaiVinubhai/DirectShare")
                }
            }
        }
    }
}

