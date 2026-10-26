# Rsser Figma Screen Migration Implementation Plan

**Goal:** Adapt Rsser's Android reading feed, subscription management, and article reader to the supplied Figma screens while preserving the Rsser app name and launcher icon.

**Architecture:** Keep the existing Compose feature modules, repositories, ViewModels, and article paging. Add subscription management as a main destination using the existing feed management state and actions, and update the feed and reader presentation to the Figma visual language. On phone widths use the four destinations shown in Figma; preserve the current adaptive navigation on wider screens.

**Tech Stack:** Kotlin, Jetpack Compose, Material 3, Navigation 3, Paging Compose.

---

### Task 1: Add the subscription destination and phone navigation

**Files:**
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/main/ui/MainScreen.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/main/ui/MainDestination.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/app/Screens.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/app/App.kt`

Add a serialized subscription route that renders a dedicated management screen. On phone widths show the Figma four-item bottom navigation; route the existing Home, Discovery, and Bookmarks destinations to 阅读, 发现, and 收藏. Keep search and settings reachable through existing app navigation and retain side navigation at large widths.

### Task 2: Build the subscription management screen

**Files:**
- Create: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/feeds/ui/SubscriptionsScreen.kt`
- Reuse: `FeedsViewModel`, `FeedsState`, `FeedsEvent`, `allSources`, feed and group edit routes.

Render the unread summary, folder section, feed rows, add-feed action, and management help card with the Figma palette and spacing. Bind list actions to the existing feed selection, edit, add, remove, and group flows.

### Task 3: Restyle the reading feed

**Files:**
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/home/ui/HomeScreen.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/home/ui/HomeTopAppBar.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/home/ui/FeaturedSection.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/home/ui/PostListItem.kt`

Use the Figma warm background, compact header and filter chips, featured article hierarchy, source metadata, bookmark affordance, and divided compact article rows. Preserve paging, refresh, unread state, and article opening behavior. Keep the visible app identity as Rsser.

### Task 4: Restyle the article reader

**Files:**
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/reader/ui/ReaderScreen.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/reader/page/ui/ReaderPage.kt`
- Modify: `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/reader/ui/ReaderViewBottomBar.kt`

Match the Figma reader chrome with a compact navigation bar, reading progress, more breathing room in article content, and a four-action bottom toolbar. Preserve Markdown rendering, paging, bookmarks, read-later, share, and typography customization.

### Task 5: Preserve Rsser identity and verify integration

**Files:**
- Inspect: `app/src/main/res/values/strings.xml`
- Inspect: `app/src/main/AndroidManifest.xml`
- Inspect: existing launcher icon resources.

Keep the app label and launcher icon unchanged. Build the FOSS debug variant to catch Kotlin and Compose integration errors.

---

## Release Follow-up Plan

**Goal:** Improve the transition from subscription management to reading, publish clear Rsser documentation with screenshots from the running Android build, and prepare the verified changes for the configured remote.

**Architecture:** Keep feed state and persistence in the existing view models. Pass a navigation callback from the main destination into the subscription manager so selecting a feed opens the reading tab. Keep branding copy in user-facing surfaces separate from code identifiers, compatibility schemes, and license notices.

**Tech Stack:** Kotlin, Jetpack Compose, Navigation 3, Gradle, Git.

### Follow-up Task 1: Complete subscription-to-reading navigation

**Files:** `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/main/ui/MainScreen.kt`, `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/main/ui/NavigationDrawerContent.kt`, `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/app/Navigation.kt`.

Pass an action from the subscription route into source rows. Dispatch the existing source selection event first, then navigate to the reading destination. Cover both normal and filtered source lists, and pinned sources.

### Follow-up Task 2: Update project documentation and runtime captures

**Files:** `README.md`, `feature/reader/src/main/kotlin/dev/sasikanth/rss/reader/about/ui/AboutScreen.kt`, `docs/screenshots/`.

Replace old user-facing product copy with Rsser copy, describe current navigation and reading workflows, add build instructions, and embed unaltered screenshots captured from the running Android build. Mark the release as modified with its date and retain the license and contributor notices.

### Follow-up Task 3: Audit and publish

Review every changed file, scan tracked and untracked source/documentation for branding and prompts, preserve required compatibility and license references, build `assembleFossDebug`, run `git diff --check`, commit only intended changes, then push the current branch to its configured upstream.
