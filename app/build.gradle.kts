plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    kotlin("android")
    kotlin("plugin.serialization") version "1.4.21"
}

android.sourceSets.all {
    java.srcDir("src/$name/kotlin")
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.2")
    defaultConfig {
        applicationId = "dev.logarithmus.quizdroid"
        minSdkVersion(16)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "0.2.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
			isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.21")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.cardview:cardview:1.0.0")

    implementation("com.google.android.gms:play-services-ads:19.6.0")
	implementation(platform("com.google.firebase:firebase-bom:26.2.0"))
	implementation("com.google.firebase:firebase-database-ktx")
}
