plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.serialization)
}

android {
  namespace = "dev.sasikanth.rss.reader.core.network"
  compileSdk = libs.versions.android.sdk.compile.get().toInt()
  defaultConfig { minSdk = libs.versions.android.sdk.min.get().toInt() }
  flavorDimensions += "version"
  productFlavors {
    create("full") { dimension = "version" }
    create("foss") { dimension = "version" }
  }
}

dependencies {
  implementation(project(":core:model"))
  implementation(project(":core:base"))
  implementation(libs.kotlinx.datetime)
  implementation(libs.kotlinx.coroutines)
  implementation(libs.kotlinx.io)
  implementation(libs.kotlininject.runtime)
  implementation(libs.ktor.core)
  implementation(libs.ktor.client.logging)
  implementation(libs.ktor.content.negotiation)
  implementation(libs.ktor.json)
  implementation(libs.ktor.resources)
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.ksoup)
  implementation(libs.ksoup.kotlinx.io)
  implementation(libs.ktxml)
  implementation(libs.kermit)
  api(libs.korlibs.string)
  implementation(libs.androidx.annotation)
  implementation(libs.ktor.client.okhttp)
  "fullImplementation"(libs.crashkios.bugsnag)
  testImplementation(libs.kotlin.test)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.ktor.client.mock)
}
