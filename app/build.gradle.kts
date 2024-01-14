plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.denizk0461.weserplaner"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.denizk0461.weserplaner"
        minSdk = 24
        targetSdk = 34
        versionCode = 13
        versionName = "1.2.3"

        resourceConfigurations += arrayOf("en", "de")

        buildConfigField("long", "BUILD_TIME_MILLIS", "${System.currentTimeMillis()}L")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions.annotationProcessorOptions {
            arguments += mapOf("room.schemaLocation" to "${projectDir}/schemas")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug") // shouldn't this be "release"?

            ndk {
                debugSymbolLevel = "SYMBOL_TABLE"
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.fragment:fragment-ktx:1.7.0-alpha06")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.4")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.4")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("androidx.room:room-runtime:2.5.2")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")

    kapt("androidx.room:room-compiler:2.5.2")

    implementation("com.google.android.material:material:1.10.0")

    implementation("com.github.alamkanak:android-week-view:1.2.6")

    implementation("org.jsoup:jsoup:1.15.4")
}