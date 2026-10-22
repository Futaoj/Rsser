plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.kotlin.parcelize)
  alias(libs.plugins.ksp)
}

kotlin {
  compilerOptions {
    optIn.add("androidx.compose.material3.ExperimentalMaterial3Api")
    optIn.add("kotlinx.coroutines.ExperimentalCoroutinesApi")
    optIn.add("org.jetbrains.compose.resources.ExperimentalResourceApi")
    optIn.add("kotlin.time.ExperimentalTime")
  }
}

android {
  namespace = "dev.sasikanth.rss.reader.common"
  compileSdk = libs.versions.android.sdk.compile.get().toInt()
  defaultConfig { minSdk = libs.versions.android.sdk.min.get().toInt() }
  flavorDimensions += "version"
  productFlavors {
    create("full") { dimension = "version" }
    create("foss") { dimension = "version" }
  }
}

dependencies {
  api(project(":core:base"))
  api(project(":core:model"))
  api(project(":core:network"))
  api(project(":core:data"))
  implementation(project(":core:icons"))
  implementation(project(":core:resources"))
  implementation(libs.bundles.compose)
  implementation(libs.compose.material3.windowsizeclass)
  implementation(libs.bundles.kotlinx)
  implementation(libs.ktor.core)
  implementation(libs.ktor.client.logging)
  implementation(libs.kotlininject.runtime)
  implementation(libs.androidx.collection)
  implementation(libs.androidx.graphics.shapes)
  implementation(libs.ksoup)
  implementation(libs.ksoup.kotlinx.io)
  api(libs.androidx.datastore.okio)
  api(libs.androidx.datastore.preferences)
  implementation(libs.paging.common)
  implementation(libs.paging.compose)
  implementation(libs.stately.isolate)
  implementation(libs.stately.iso.collections)
  implementation(libs.bundles.xmlutil)
  implementation(libs.uuid)
  api(libs.coil.compose)
  api(libs.coil.network)
  api(libs.coil.svg)
  implementation(libs.kermit)
  implementation(libs.reorderable)
  implementation(libs.landscapist.image)
  implementation(libs.landscapist.coil3)
  implementation(libs.landscapist.zoomable)
  implementation(libs.markdown.renderer)
  implementation(libs.markdown.material3)
  implementation(libs.markdown.coil)
  implementation(libs.markdown.code)
  implementation(libs.viewmodel)
  implementation(libs.lifecycle.runtime.compose)
  api(libs.jetbrains.navigation3.ui)
  api(libs.androidx.navigationevent.compose)
  api(libs.compose.material3.adaptive.navigation3)
  api(libs.jetbrains.lifecycle.viewmodelNavigation3)
  implementation(libs.material.kolor)
  api(libs.androidx.activity.compose)
  api(libs.androidx.appcompat)
  api(libs.androidx.core)
  api(libs.androidx.browser)
  implementation(libs.ktor.client.okhttp)
  "fullImplementation"(libs.crashkios.bugsnag)
  "fullImplementation"(libs.kermit.bugsnag)
  api(libs.androidx.media3.exoplayer)
  api(libs.androidx.media3.session)
  api(libs.androidx.media3.ui)
  implementation(libs.kotlinx.coroutines.guava)
  "fullImplementation"(libs.googlePlayReview)
  debugImplementation(libs.compose.ui.tooling)
  testImplementation(libs.kotlin.test)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.ktor.client.mock)
  add("kspFull", libs.kotlininject.compiler)
  add("kspFoss", libs.kotlininject.compiler)
}
