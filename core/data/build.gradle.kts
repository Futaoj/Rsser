plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.sqldelight)
  alias(libs.plugins.kotlin.serialization)
}

android {
  namespace = "dev.sasikanth.rss.reader.data"
  compileSdk = libs.versions.android.sdk.compile.get().toInt()
  defaultConfig { minSdk = libs.versions.android.sdk.min.get().toInt() }
  flavorDimensions += "version"
  productFlavors {
    create("full") { dimension = "version" }
    create("foss") { dimension = "version" }
  }
}

dependencies {
  api(project(":core:model"))
  api(project(":core:base"))
  api(project(":core:network"))
  implementation(libs.kotlinx.datetime)
  implementation(libs.kotlinx.coroutines)
  implementation(libs.kotlinx.immutable.collections)
  implementation(libs.kotlininject.runtime)
  implementation(libs.kermit)
  api(libs.sqldelight.extensions.coroutines)
  api(libs.sqldelight.extensions.paging)
  api(libs.androidx.datastore.preferences)
  api(libs.androidx.datastore.okio)
  api(libs.uuid)
  api(libs.ktor.core)
  implementation(libs.ktor.resources)
  implementation(libs.ktor.content.negotiation)
  implementation(libs.ktor.json)
  api(libs.kotlinx.serialization.json)
  implementation(libs.filekit.core)
  implementation(libs.filekit.dialogs)
  implementation(libs.ksoup)
  implementation(libs.bundles.xmlutil)
  implementation(libs.stately.isolate)
  implementation(libs.androidx.annotation)
  implementation(libs.sqldelight.driver.android)
  api(libs.sqliteAndroid)
  "fullImplementation"(libs.crashkios.bugsnag)
  testImplementation(libs.kotlin.test)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.ktor.client.mock)
  testImplementation(libs.ktor.resources)
}

sqldelight {
  databases {
    create("ReaderDatabase") {
      packageName.set("dev.sasikanth.rss.reader.data.database")
      dialect(libs.sqldelight.sqlite.dialect)
      schemaOutputDirectory.set(file("src/main/sqldelight/databases"))
      verifyMigrations.set(true)
    }
  }
}
