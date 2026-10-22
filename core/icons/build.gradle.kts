plugins { alias(libs.plugins.android.library) }

android {
  namespace = "dev.sasikanth.rss.reader.resources.icons"
  compileSdk = libs.versions.android.sdk.compile.get().toInt()
  defaultConfig { minSdk = libs.versions.android.sdk.min.get().toInt() }
  flavorDimensions += "version"
  productFlavors {
    create("full") { dimension = "version" }
    create("foss") { dimension = "version" }
  }
}

dependencies {
  implementation(libs.compose.foundation)
}
