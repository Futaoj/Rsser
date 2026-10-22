# Twine Android

Android-only RSS reader project, organized as a conventional Gradle multi-module application.

## Modules

- `app`: Android application entry point, manifest, widgets, and build variants.
- `feature/reader`: Compose UI and presentation logic.
- `core/base`: shared utilities and Android platform abstractions.
- `core/model`: domain models.
- `core/network`: feed fetching and parsing.
- `core/data`: repositories, sync, and SQLDelight database.
- `core/icons`: app icon composables.
- `core/resources`: Compose resources compiled for Android and consumed by the reader feature.

## Requirements

- Android Studio with Android SDK Platform 37 installed.
- JDK 21. Android Studio's bundled runtime can be selected in Gradle settings.

## Build

Open this directory (`twine_a`) in Android Studio and allow Gradle sync to finish. Select the `app` run configuration and an Android emulator, then run it.

From a terminal configured with JDK 21:

```sh
./gradlew assembleFossDebug
```

The app has `full` and `foss` product flavors. The FOSS debug variant can be installed without release signing credentials. To build the full variant, use `assembleFullDebug`.
