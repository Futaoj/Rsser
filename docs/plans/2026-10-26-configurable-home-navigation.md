# Configurable Home Navigation Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Let users configure four ordered phone navigation entries from seven destinations, including AI chat, and add AI chat to the side navigation.

**Architecture:** Persist an ordered, validated string list in the existing Preferences DataStore via `SettingsRepository`. Expose it through `AppViewModel`/`AppState` for immediate main-screen updates and through `SettingsViewModel`/`SettingsState` for the configuration screen. Keep AI chat as a separate outer-navigation action while making it a configurable phone tab.

**Tech Stack:** Kotlin, Jetpack Compose Material 3, Navigation 3, Preferences DataStore, existing ViewModels.

---

### Task 1: Persist and expose the four-entry selection

**Files:**
- Modify: `core/data/src/main/kotlin/dev/sasikanth/rss/reader/data/repository/SettingsRepository.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/app/AppState.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/app/AppViewModel.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/settings/SettingsState.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/settings/SettingsViewModel.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/settings/SettingsEvent.kt`

Add a preference containing exactly four distinct stable destination IDs. Default to Home, Subscriptions, Discovery, Bookmarks. Invalid or missing values resolve to this default. Emit updates to app and settings state and handle a settings event that persists a new order.

### Task 2: Add the navigation customization screen

**Files:**
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/app/Screens.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/app/App.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/app/Navigation.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/settings/ui/SettingsScreen.kt`
- Create: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/settings/ui/SettingsBottomNavigationScreen.kt`
- Modify: `core/resources/src/commonMain/composeResources/values/strings.xml`
- Modify: `core/resources/src/commonMain/composeResources/values-zh/strings.xml`

Add a Settings item and dedicated page with four ordered selectors. Each selector offers only choices not used by the other slots. Persist changes immediately and include an AI chat choice.

### Task 3: Render configured tabs and add the AI side-navigation action

**Files:**
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/main/ui/MainScreen.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/main/ui/MainDestination.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/main/ui/NavigationDrawerContent.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/app/Navigation.kt`

Replace the fixed phone destination list with the persisted order. Map six entries to the existing nested main routes and dispatch AI chat to the outer navigator. Add AI chat rows to expanded and collapsed side navigation and close modal navigation before opening the existing chat route.

### Task 4: Verify integration

Run `./gradlew :app:assembleFossDebug --no-daemon` and `git diff --check`. Review the changed state flow, route callbacks, default order, and choice uniqueness. Do not add or run automated tests unless requested.
