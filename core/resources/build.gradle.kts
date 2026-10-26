plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.kmp.library)
  alias(libs.plugins.compose)
  alias(libs.plugins.kotlin.compose)
}

kotlin {
  android {
    namespace = "dev.sasikanth.rss.reader.resources"
    compileSdk = libs.versions.android.sdk.compile.get().toInt()
    minSdk = libs.versions.android.sdk.min.get().toInt()
    androidResources { enable = true }
  }
  sourceSets {
    commonMain.dependencies {
      implementation(libs.compose.runtime)
      implementation(libs.compose.resources)
    }
  }
}

compose.resources {
  packageOfResClass = "rsser.shared.generated.resources"
  publicResClass = true
  generateResClass = always
}
