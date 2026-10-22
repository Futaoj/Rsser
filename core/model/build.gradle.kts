plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.serialization)
}

android {
  namespace = "dev.sasikanth.rss.reader.core.model"
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
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.compose.runtime)
  implementation(libs.uuid)
  implementation(libs.kotlinx.immutable.collections)
  testImplementation(libs.kotlin.test)
}
