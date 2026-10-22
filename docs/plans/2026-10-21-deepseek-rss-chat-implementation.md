# DeepSeek RSS Chat Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Replace the pinned-sources home bottom bar with an AI entry and provide a DeepSeek chat grounded in recent subscribed RSS titles and summaries.

**Architecture:** Add a bounded local recent-post query to `core:data`, a DeepSeek API client and encrypted API-key storage in the Android reader feature, and a chat route/screen wired from the home bottom bar. Preserve source pin controls and active audio controls while removing the obsolete bottom-bar preference.

**Tech Stack:** Kotlin, Jetpack Compose, Navigation 3, Ktor, SQLDelight, Android Keystore, DataStore, kotlinx.serialization.

---

### Task 1: Add a bounded recent RSS context query

**Files:**
- Modify: `core/data/src/main/sqldelight/dev/sasikanth/rss/reader/data/database/Post.sq`
- Modify: `core/data/src/main/kotlin/dev/sasikanth/rss/reader/data/repository/PostRepository.kt`
- Modify: `core/data/src/main/kotlin/dev/sasikanth/rss/reader/data/repository/RssRepository.kt`

Steps:
1. Add a query returning recent non-deleted, non-blocked posts joined to feed names, ordered by `postDate DESC`, with a caller-supplied hard-limited count.
2. Expose a suspend repository method that maps rows to a small context model containing feed name, title, description, date, and link.
3. Clamp the maximum count in repository code to a conservative fixed cap (initially 20), regardless of caller input.
4. Include only titles and summaries; never load `postContent.articleContent` for this feature.

### Task 2: Implement DeepSeek transport and protected key storage

**Files:**
- Create: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/ai/DeepSeekClient.kt`
- Create: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/ai/DeepSeekModels.kt`
- Create: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/ai/DeepSeekKeyStore.android.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/di/SharedApplicationComponent.kt`
- Modify: `feature/reader/build.gradle.kts` only if an existing Ktor or serialization dependency is not already exposed.

Steps:
1. Define serializable chat completion request/response models for `model`, `messages`, `choices`, and provider errors.
2. Implement a Ktor POST to `https://api.deepseek.com/chat/completions` with bearer authorization and JSON content negotiation compatible with the existing client setup.
3. Use the currently documented DeepSeek chat model name, keeping it in one constant so it can be updated in one place.
4. Implement Android Keystore-backed AES-GCM encryption for the API key and store only the ciphertext in app-private preferences.
5. Ensure key values and authorization headers are never logged; translate HTTP 401, 429, and other failures to typed errors.

### Task 3: Add AI chat state and behavior

**Files:**
- Create: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/ai/AiChatState.kt`
- Create: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/ai/AiChatEvent.kt`
- Create: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/ai/AiChatViewModel.kt`

Steps:
1. Define message, loading, key-configuration, first-use disclosure, and error state.
2. Let users save/update/remove their API key without exposing the stored value back to UI state.
3. On send, load at most 20 recent posts and cap each title/summary/link context length before building the system prompt.
4. Send the system context and current in-memory conversation to DeepSeek; append the response only on success and expose recoverable localized errors on failure.
5. Keep conversation history in memory only; clear it when the user starts a new conversation or the ViewModel is cleared.

### Task 4: Build the localized AI conversation screen

**Files:**
- Create: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/ai/ui/AiChatScreen.kt`
- Modify: `core/resources/src/commonMain/composeResources/values/strings.xml`
- Modify: `core/resources/src/commonMain/composeResources/values-zh/strings.xml`

Steps:
1. Build a Chinese chat interface with app bar/back, user and assistant bubbles, composer, send button, loading indicator, and clear-conversation action.
2. Add API-key entry/edit UI and explain that recent RSS titles and summaries are sent to DeepSeek before the first request.
3. Render empty-feed and transport/auth/rate-limit failures with actionable Chinese messages.
4. Keep the layout scrollable and handle IME/system insets.

### Task 5: Wire navigation and replace the home bottom bar

**Files:**
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/app/Screens.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/app/Navigation.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/main/ui/MainScreen.kt` as required for a route callback.
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/home/ui/HomeScreen.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/feeds/ui/pinned/PinnedSourcesBottomBar.kt` only if shared media controls must be extracted before replacing the component.

Steps:
1. Add a serializable AI chat route and register the route provider/ViewModel factory.
2. Replace the home pinned-sources bottom bar with an AI entry at the same position.
3. Preserve active now-playing controls in the bottom region and keep source pin/unpin actions in the drawer.
4. Remove the obsolete “show pinned sources” preference from SettingsRepository, HomeState/HomeViewModel, and the settings UI.

### Task 6: Verify the Android build and inspect the final diff

**Files:**
- Review all files listed above.

Steps:
1. Run `./gradlew :feature:reader:compileFullDebugKotlin :app:assembleFullDebug`.
2. Resolve any compilation/resource-generation issues.
3. Inspect the changed files to confirm no plaintext API key, logging of secrets, unbounded context, or obsolete pinned-bar setting remains.
4. Report build result and note that runtime DeepSeek calls require the user to enter their own API key.
