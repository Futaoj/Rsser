# Remove account selection Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Remove the account-selection screen and make the existing welcome CTA open first-run Discover directly.

**Architecture:** Keep `PlaceholderViewModel` startup routing and the onboarding completion preference. Change the onboarding effect/navigation to go directly to `Screen.Discovery(isFromOnboarding = true)`, then remove the now-unused account-selection route, UI, ViewModel files, and translated strings. Keep cloud sync implementations and Settings provider login routes.

**Tech Stack:** Kotlin, Compose Multiplatform, Navigation 3, Compose resources, Gradle.

---

### Task 1: Route onboarding directly to Discover

**Files:**
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/onboarding/OnboardingEffect.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/onboarding/OnboardingViewModel.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/onboarding/ui/OnboardingScreen.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/app/Navigation.kt`

Replace the onboarding effect and callback named for account selection with discovery navigation. The callback must open `Screen.Discovery(isFromOnboarding = true)`, preserving the existing Discover completion behavior that marks onboarding complete and returns to Home.

### Task 2: Remove account-selection navigation and implementation

**Files:**
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/app/Screens.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/app/Navigation.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/app/App.kt`
- Delete: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/accountselection/AccountSelectionEffect.kt`
- Delete: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/accountselection/AccountSelectionEvent.kt`
- Delete: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/accountselection/AccountSelectionState.kt`
- Delete: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/accountselection/AccountSelectionViewModel.kt`
- Delete: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/accountselection/ui/AccountSelectionScreen.kt`

Remove `Screen.AccountSelection`, its entry-provider function and registration, and its App ViewModel factory parameter/import. Do not remove cloud sync or provider login flows still reachable from Settings.

### Task 3: Remove account-selection translations

**Files:**
- Modify: `core/resources/src/commonMain/composeResources/values/strings.xml`
- Modify: `core/resources/src/commonMain/composeResources/values-de/strings.xml`
- Modify: `core/resources/src/commonMain/composeResources/values-fr/strings.xml`
- Modify: `core/resources/src/commonMain/composeResources/values-hi/strings.xml`
- Modify: `core/resources/src/commonMain/composeResources/values-ru/strings.xml`
- Modify: `core/resources/src/commonMain/composeResources/values-tr/strings.xml`
- Modify: `core/resources/src/commonMain/composeResources/values-zh/strings.xml`

Delete only the five `accountSelection*` string resources from each locale.

### Task 4: Review the resulting source

Search the Kotlin and Compose resource source trees for `AccountSelection` and `accountSelection`; remove any remaining references. Preserve startup handling for returning users and provider login navigation under Settings. The caller did not request tests or a build, so do not run them.
