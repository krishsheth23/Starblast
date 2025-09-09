import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android") // ✅ Better than "kotlin-android" in Kotlin DSL
    id("dev.flutter.flutter-gradle-plugin")
}

android {
    namespace = "com.example.cosmic_havoc"
    compileSdk = flutter.compileSdkVersion
    ndkVersion = flutter.ndkVersion

    // Load signing properties from key.properties (optional, not checked into VCS)
    // Prefer the module-level `key.properties` (android/app/key.properties).
    val moduleKeystoreFile = file("key.properties")
    val rootKeystoreFile = rootProject.file("key.properties")
    val keystorePropertiesFile = if (moduleKeystoreFile.exists()) moduleKeystoreFile else rootKeystoreFile
    val keystoreProperties = Properties()
    if (keystorePropertiesFile.exists()) {
        FileInputStream(keystorePropertiesFile).use { fis ->
            keystoreProperties.load(fis)
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    defaultConfig {
        applicationId = "com.game.cosmic_havoc"
        minSdk = flutter.minSdkVersion
        targetSdk = flutter.targetSdkVersion
        versionCode = flutter.versionCode
        versionName = flutter.versionName
    }

    buildTypes {
        release {
            signingConfig = if (keystorePropertiesFile.exists()) {
                signingConfigs.create("release") {
                    keyAlias = keystoreProperties.getProperty("keyAlias")
                    keyPassword = keystoreProperties.getProperty("keyPassword")

                    // Prefer an explicit `storeFile` property; if missing, try local `star_blast_key.jks`.
                    val storeFileProp = keystoreProperties.getProperty("storeFile")
                    if (!storeFileProp.isNullOrEmpty()) {
                        storeFile = file(storeFileProp)
                    } else {
                        val localJks = file("star_blast_key.jks")
                        if (localJks.exists()) {
                            storeFile = localJks
                        }
                    }

                    storePassword = keystoreProperties.getProperty("storePassword")
                }
                signingConfigs.getByName("release")
            } else {
                signingConfigs.getByName("debug")
            }
        }
    }
}

flutter {
    source = "../.."
}
