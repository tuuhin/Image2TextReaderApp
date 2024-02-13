plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("app.cash.sqldelight") version "2.0.1"
}

android {
	namespace = "com.eva.image2textreader"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.eva.image2textreader"
		minSdk = 26
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
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
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}
	buildFeatures {
		compose = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.1"
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}

dependencies {

	// core | compose | lifecycle
	implementation("androidx.core:core-ktx:1.12.0")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
	implementation("androidx.activity:activity-compose:1.8.2")
	//compose
	val composeBom = platform("androidx.compose:compose-bom:2024.02.00")
	implementation(composeBom)
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-graphics")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.material3:material3")
	//play-service-ml
	implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.0")
	//koin
	implementation("io.insert-koin:koin-android:3.5.3")
	implementation("io.insert-koin:koin-androidx-compose:3.5.3")
	//coil
	implementation("io.coil-kt:coil-compose:2.5.0")
	//navigation
	implementation("androidx.navigation:navigation-compose:2.7.6")
	// kotlin -datetime
	implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
	// kotlin -immutable
	implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.7")
	//lifecycle-runtime
	implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
	//splash
	implementation("androidx.core:core-splashscreen:1.0.1")
	//sql-delight-android-driver
	implementation("app.cash.sqldelight:android-driver:2.0.1")
	implementation("app.cash.sqldelight:coroutines-extensions:2.0.1")
	implementation("app.cash.sqldelight:primitive-adapters:2.0.0-alpha05")
	// test
	testImplementation("junit:junit:4.13.2")
	// instrumented test
	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
	androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
	androidTestImplementation("androidx.compose.ui:ui-test-junit4")
	//debug
	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")
}

sqldelight {
	databases {
		create("AppDataBase") {
			packageName.set("com.eva")
		}
	}
}