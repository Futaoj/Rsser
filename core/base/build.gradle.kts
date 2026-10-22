plugins { alias(libs.plugins.android.library) }

android {
  namespace = "dev.sasikanth.rss.reader.core.base"
  compileSdk = libs.versions.android.sdk.compile.get().toInt()
  defaultConfig { minSdk = libs.versions.android.sdk.min.get().toInt() }
  flavorDimensions += "version"
  productFlavors {
    create("full") { dimension = "version" }
    create("foss") { dimension = "version" }
  }
}

dependencies {
  implementation(libs.kotlinx.datetime)
  implementation(libs.kotlinx.coroutines)
  implementation(libs.kotlininject.runtime)
  api(libs.uuid)
  implementation(libs.androidx.annotation)
  api(libs.androidx.activity.compose)
  api(libs.androidx.core)
  "fullImplementation"(libs.crashkios.bugsnag)
  "fullImplementation"(libs.bugsnag)
  testImplementation(libs.kotlin.test)
}
