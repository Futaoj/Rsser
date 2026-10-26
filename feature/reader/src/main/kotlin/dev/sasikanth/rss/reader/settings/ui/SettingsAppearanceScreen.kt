/*
 * Copyright 2026 Sasikanth Miriyampalli
 *
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/gpl-3.0.en.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package dev.sasikanth.rss.reader.settings.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import dev.sasikanth.rss.reader.app.AppInfo
import dev.sasikanth.rss.reader.app.AppPlatform
import dev.sasikanth.rss.reader.components.DropdownMenu
import dev.sasikanth.rss.reader.components.DropdownMenuItem
import dev.sasikanth.rss.reader.components.SimpleTopAppBar
import dev.sasikanth.rss.reader.components.SubHeader
import dev.sasikanth.rss.reader.components.ToggleableButtonGroup
import dev.sasikanth.rss.reader.components.ToggleableButtonItem
import dev.sasikanth.rss.reader.components.TranslucentButton
import dev.sasikanth.rss.reader.core.model.local.ResolvedPost
import dev.sasikanth.rss.reader.core.model.local.ThemeVariant
import dev.sasikanth.rss.reader.data.repository.AppThemeMode
import dev.sasikanth.rss.reader.data.repository.HomeViewMode
import dev.sasikanth.rss.reader.home.ui.CompactPostListItem
import dev.sasikanth.rss.reader.home.ui.PostListItem
import dev.sasikanth.rss.reader.home.ui.SimplePostListItem
import dev.sasikanth.rss.reader.resources.icons.ArrowDown
import dev.sasikanth.rss.reader.resources.icons.RsserIcons
import dev.sasikanth.rss.reader.resources.icons.platform
import dev.sasikanth.rss.reader.settings.SettingsEvent
import dev.sasikanth.rss.reader.settings.SettingsState
import dev.sasikanth.rss.reader.settings.SettingsViewModel
import dev.sasikanth.rss.reader.ui.AppTheme
import dev.sasikanth.rss.reader.utils.LocalShowFeedFavIconSetting
import dev.sasikanth.rss.reader.utils.LocalWindowSizeClass
import dev.sasikanth.rss.reader.utils.restrictContentWidth
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import org.jetbrains.compose.resources.stringResource
import rsser.shared.generated.resources.Res
import rsser.shared.generated.resources.homeViewMode
import rsser.shared.generated.resources.homeViewModeCompact
import rsser.shared.generated.resources.homeViewModeDefault
import rsser.shared.generated.resources.homeViewModeSimple
import rsser.shared.generated.resources.other
import rsser.shared.generated.resources.settingsAppearanceAndLayout
import rsser.shared.generated.resources.settingsHeaderTheme
import rsser.shared.generated.resources.settingsShowFeaturedSectionSubtitle
import rsser.shared.generated.resources.settingsShowFeaturedSectionTitle
import rsser.shared.generated.resources.settingsShowUnreadCountSubtitle
import rsser.shared.generated.resources.settingsShowUnreadCountTitle
import rsser.shared.generated.resources.settingsThemeAuto
import rsser.shared.generated.resources.settingsThemeDark
import rsser.shared.generated.resources.settingsThemeLight
import rsser.shared.generated.resources.showFeedFavIconDesc
import rsser.shared.generated.resources.showFeedFavIconTitle
import rsser.shared.generated.resources.themeVariantAmber
import rsser.shared.generated.resources.themeVariantCoral
import rsser.shared.generated.resources.themeVariantDynamic
import rsser.shared.generated.resources.themeVariantForest
import rsser.shared.generated.resources.themeVariantLavender
import rsser.shared.generated.resources.themeVariantParchment
import rsser.shared.generated.resources.themeVariantRaspberry
import rsser.shared.generated.resources.themeVariantSepia
import rsser.shared.generated.resources.themeVariantSkyline
import rsser.shared.generated.resources.themeVariantSlate
import rsser.shared.generated.resources.themeVariantSolarized
import rsser.shared.generated.resources.themeVariantSystemDynamic

@Composable
internal fun SettingsAppearanceScreen(
  viewModel: SettingsViewModel,
  goBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val state by viewModel.state.collectAsStateWithLifecycle()

  SettingsAppearanceContent(
    state = state,
    dispatch = viewModel::dispatch,
    goBack = goBack,
    modifier = modifier,
  )
}

@Composable
private fun SettingsAppearanceContent(
  state: SettingsState,
  dispatch: (SettingsEvent) -> Unit,
  goBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val layoutDirection = LocalLayoutDirection.current

  Scaffold(
    modifier = modifier,
    topBar = {
      SimpleTopAppBar(
        title = stringResource(Res.string.settingsAppearanceAndLayout),
        onBackClick = goBack,
      )
    },
    containerColor = AppTheme.colorScheme.backdrop,
    contentColor = Color.Unspecified,
    content = { padding ->
      LazyColumn(
        modifier = Modifier.restrictContentWidth(),
        contentPadding =
          PaddingValues(
            start = padding.calculateStartPadding(layoutDirection),
            top = padding.calculateTopPadding() + 8.dp,
            end = padding.calculateEndPadding(layoutDirection),
            bottom = padding.calculateBottomPadding() + 80.dp,
          ),
      ) {
        item { SubHeader(text = stringResource(Res.string.settingsHeaderTheme)) }

        item {
          ThemeModeSelection(
            selectedThemeVariant = dev.sasikanth.rss.reader.core.model.local.ThemeVariant.Forest,
            appThemeMode = state.appThemeMode,
            onAppThemeModeChanged = { dispatch(SettingsEvent.OnAppThemeModeChanged(it)) },
          )
        }

        item { SubHeader(text = stringResource(Res.string.homeViewMode)) }

        item {
          LayoutSelection(
            homeViewMode = state.homeViewMode,
            onLayoutChanged = { dispatch(SettingsEvent.ChangeHomeViewMode(it)) },
          )
        }

        item { SubHeader(text = stringResource(Res.string.other)) }

        item {
          SettingsSwitchItem(
            title = stringResource(Res.string.settingsShowFeaturedSectionTitle),
            subtitle = stringResource(Res.string.settingsShowFeaturedSectionSubtitle),
            checked = state.showFeaturedSection,
            onValueChanged = { dispatch(SettingsEvent.ToggleShowFeaturedSection(it)) },
          )
        }

        item {
          SettingsSwitchItem(
            title = stringResource(Res.string.settingsShowUnreadCountTitle),
            subtitle = stringResource(Res.string.settingsShowUnreadCountSubtitle),
            checked = state.showUnreadPostsCount,
            onValueChanged = { dispatch(SettingsEvent.ToggleShowUnreadPostsCount(it)) },
          )
        }

        item {
          SettingsSwitchItem(
            title = stringResource(Res.string.showFeedFavIconTitle),
            subtitle = stringResource(Res.string.showFeedFavIconDesc),
            checked = state.showFeedFavIcon,
            onValueChanged = { dispatch(SettingsEvent.ToggleShowFeedFavIcon(it)) },
          )
        }
      }
    },
  )
}

@Composable
private fun ThemeModeSelection(
  selectedThemeVariant: ThemeVariant,
  appThemeMode: AppThemeMode,
  onAppThemeModeChanged: (AppThemeMode) -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier.fillMaxWidth().padding(horizontal = 24.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Text(
      text = "纸感 · Rsser",
      style = MaterialTheme.typography.titleLarge,
      color = AppTheme.colorScheme.onSurface,
      fontWeight = FontWeight.SemiBold,
    )

    ThemeModeSelector(appThemeMode = appThemeMode, onAppThemeModeChanged = onAppThemeModeChanged)
  }
}

@Composable
private fun ThemeModeSelector(
  appThemeMode: AppThemeMode,
  onAppThemeModeChanged: (AppThemeMode) -> Unit,
) {
  var showDropdown by remember { mutableStateOf(false) }

  Box {
    TranslucentButton(
      text = appThemeMode.displayName(),
      trailingIcon = RsserIcons.ArrowDown,
      onClick = { showDropdown = true },
    )

    DropdownMenu(
      expanded = showDropdown,
      onDismissRequest = { showDropdown = false },
      modifier = Modifier.widthIn(min = 96.dp),
    ) {
      AppThemeMode.entries.forEach { mode ->
        DropdownMenuItem(
          text = mode.displayName(),
          selected = mode == appThemeMode,
          onClick = {
            onAppThemeModeChanged(mode)
            showDropdown = false
          },
        )
      }
    }
  }
}

@Composable
private fun LayoutSelection(homeViewMode: HomeViewMode, onLayoutChanged: (HomeViewMode) -> Unit) {
  Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp)) {
    ToggleableButtonGroup(
      items =
        listOf(
          ToggleableButtonItem(
            label = stringResource(Res.string.homeViewModeDefault),
            isSelected = homeViewMode == HomeViewMode.Default,
            identifier = HomeViewMode.Default,
          ),
          ToggleableButtonItem(
            label = stringResource(Res.string.homeViewModeSimple),
            isSelected = homeViewMode == HomeViewMode.Simple,
            identifier = HomeViewMode.Simple,
          ),
          ToggleableButtonItem(
            label = stringResource(Res.string.homeViewModeCompact),
            isSelected = homeViewMode == HomeViewMode.Compact,
            identifier = HomeViewMode.Compact,
          ),
        ),
      onItemSelected = { onLayoutChanged(it.identifier as HomeViewMode) },
    )

    Spacer(Modifier.requiredHeight(16.dp))

    LayoutPreview(homeViewMode = homeViewMode)
  }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun LayoutPreview(homeViewMode: HomeViewMode) {
  val timeZone = TimeZone.currentSystemDefault()
  val mockPost =
    ResolvedPost(
      id = "1",
      sourceId = "1",
      title = "Phones are going to get weird next week",
      description = "Expect rotating camera rings, robot arms, and magnetic modules at MWC 2026...",
      link = "https://www.theverge.com/tech/885287/mwc-2026-what-to-expect-phones-mobile-weird",
      imageUrl =
        "https://platform.theverge.com/wp-content/uploads/sites/2/2026/02/honor-robot-phone.jpg?quality=90&strip=all&crop=0%2C0.011361054305837%2C100%2C99.977277891388&w=1440",
      audioUrl = null,
      date = LocalDate(2026, 2, 27).atStartOfDayIn(timeZone),
      createdAt = LocalDate(2026, 2, 27).atStartOfDayIn(timeZone),
      commentsLink = null,
      flags = emptySet(),
      feedName = "The Verge",
      feedIcon = "https://theverge.com/icon.png",
      feedHomepageLink =
        "https://platform.theverge.com/wp-content/uploads/sites/2/2025/01/verge-rss-large_80b47e.png?w=150&h=150&crop=1",
      alwaysFetchFullArticle = false,
      showFeedFavIcon = LocalShowFeedFavIconSetting.current,
      feedContentReadingTime = 2,
    )

  Box(
    modifier =
      Modifier.fillMaxWidth()
        .border(
          width = 1.dp,
          color = AppTheme.colorScheme.outlineVariant,
          shape = MaterialTheme.shapes.largeIncreased,
        )
        .padding(vertical = 8.dp)
  ) {
    BoxWithConstraints {
      val previewSizeClass =
        remember(maxWidth, maxHeight) {
          WindowSizeClass(widthDp = maxWidth.value, heightDp = maxHeight.value)
        }
      CompositionLocalProvider(LocalWindowSizeClass provides previewSizeClass) {
        AnimatedContent(homeViewMode) {
          when (it) {
            HomeViewMode.Default -> {
              PostListItem(
                item = mockPost,
                onClick = {},
                onPostBookmarkClick = {},
                onPostCommentsClick = {},
                onPostSourceClick = {},
                updatePostReadStatus = {},
              )
            }
            HomeViewMode.Simple -> {
              SimplePostListItem(
                item = mockPost,
                onClick = {},
                onPostBookmarkClick = {},
                onPostCommentsClick = {},
                onPostSourceClick = {},
                updatePostReadStatus = {},
              )
            }
            HomeViewMode.Compact -> {
              CompactPostListItem(
                item = mockPost,
                onClick = {},
                onPostBookmarkClick = {},
                onPostCommentsClick = {},
                updatePostReadStatus = {},
              )
            }
          }
        }
      }
    }

    Box(
      modifier =
        Modifier.matchParentSize()
          .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = {
              // no-op
            },
          )
    )
  }
}

@Composable
private fun ThemeVariant.displayName(): String {
  return when (this) {
    ThemeVariant.Dynamic -> stringResource(Res.string.themeVariantDynamic)
    ThemeVariant.SystemDynamic -> stringResource(Res.string.themeVariantSystemDynamic)
    ThemeVariant.Solarized -> stringResource(Res.string.themeVariantSolarized)
    ThemeVariant.Forest -> stringResource(Res.string.themeVariantForest)
    ThemeVariant.Amber -> stringResource(Res.string.themeVariantAmber)
    ThemeVariant.Coral -> stringResource(Res.string.themeVariantCoral)
    ThemeVariant.Raspberry -> stringResource(Res.string.themeVariantRaspberry)
    ThemeVariant.Skyline -> stringResource(Res.string.themeVariantSkyline)
    ThemeVariant.Parchment -> stringResource(Res.string.themeVariantParchment)
    ThemeVariant.Sepia -> stringResource(Res.string.themeVariantSepia)
    ThemeVariant.Slate -> stringResource(Res.string.themeVariantSlate)
    ThemeVariant.Lavender -> stringResource(Res.string.themeVariantLavender)
  }
}

@Composable
private fun AppThemeMode.displayName(): String {
  return when (this) {
    AppThemeMode.Auto -> stringResource(Res.string.settingsThemeAuto)
    AppThemeMode.Light -> stringResource(Res.string.settingsThemeLight)
    AppThemeMode.Dark -> stringResource(Res.string.settingsThemeDark)
  }
}

@Preview(locale = "en")
@Composable
private fun SettingsAppearancePreview() {
  AppTheme {
    SettingsAppearanceContent(
      state =
        SettingsState.default(
          appInfo =
            AppInfo(
              versionCode = 1,
              versionName = "1.0.0",
              isDebugBuild = true,
              isFoss = false,
              isGoogleDriveSupported = true,
              cachePath = { "" },
              platform = AppPlatform.Android,
            )
        ),
      dispatch = {},
      goBack = {},
    )
  }
}
