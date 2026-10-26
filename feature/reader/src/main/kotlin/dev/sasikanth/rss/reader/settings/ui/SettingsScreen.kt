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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.sasikanth.rss.reader.app.AppInfo
import dev.sasikanth.rss.reader.app.AppPlatform
import dev.sasikanth.rss.reader.components.SimpleTopAppBar
import dev.sasikanth.rss.reader.resources.icons.Account
import dev.sasikanth.rss.reader.resources.icons.Appearance
import dev.sasikanth.rss.reader.resources.icons.Behaviors
import dev.sasikanth.rss.reader.resources.icons.Changelog
import dev.sasikanth.rss.reader.resources.icons.Sync
import dev.sasikanth.rss.reader.resources.icons.RsserIcons
import dev.sasikanth.rss.reader.settings.SettingsState
import dev.sasikanth.rss.reader.settings.SettingsViewModel
import dev.sasikanth.rss.reader.settings.ui.items.SettingsNavigationItem
import dev.sasikanth.rss.reader.ui.AppTheme
import org.jetbrains.compose.resources.stringResource
import rsser.shared.generated.resources.Res
import rsser.shared.generated.resources.settings
import rsser.shared.generated.resources.settingsAppInfoAndFeedback
import rsser.shared.generated.resources.settingsAppearanceAndLayout
import rsser.shared.generated.resources.settingsAppearanceAndLayoutSubtitle
import rsser.shared.generated.resources.settingsFeaturesAndBehaviors
import rsser.shared.generated.resources.settingsFeaturesAndBehaviorsSubtitle
import rsser.shared.generated.resources.settingsServicesAndSync
import rsser.shared.generated.resources.settingsServicesAndSyncSubtitle
import rsser.shared.generated.resources.settingsYourInsights
import rsser.shared.generated.resources.settingsYourInsightsSubtitle

@Composable
internal fun SettingsScreen(
  viewModel: SettingsViewModel,
  goBack: () -> Unit,
  openAppearanceSettings: () -> Unit,
  openBehaviorSettings: () -> Unit,
  openServicesSettings: () -> Unit,
  openDataSettings: () -> Unit,
  openAppInfoSettings: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val state by viewModel.state.collectAsStateWithLifecycle()

  SettingsContent(
    state = state,
    goBack = goBack,
    openAppearanceSettings = openAppearanceSettings,
    openBehaviorSettings = openBehaviorSettings,
    openServicesSettings = openServicesSettings,
    openDataSettings = openDataSettings,
    openAppInfoSettings = openAppInfoSettings,
    modifier = modifier,
  )
}

@Composable
private fun SettingsContent(
  state: SettingsState,
  goBack: () -> Unit,
  openAppearanceSettings: () -> Unit,
  openBehaviorSettings: () -> Unit,
  openServicesSettings: () -> Unit,
  openDataSettings: () -> Unit,
  openAppInfoSettings: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val layoutDirection = LocalLayoutDirection.current
  Scaffold(
    modifier = modifier,
    topBar = { SimpleTopAppBar(title = stringResource(Res.string.settings), onBackClick = goBack) },
    content = { padding ->
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding =
          PaddingValues(
            start = padding.calculateStartPadding(layoutDirection) + settingsItemHorizontalPadding,
            top = padding.calculateTopPadding() + 8.dp,
            end = padding.calculateEndPadding(layoutDirection) + settingsItemHorizontalPadding,
            bottom = padding.calculateBottomPadding() + 80.dp,
          ),
      ) {
        item {
          SettingsNavigationItem(
            title = stringResource(Res.string.settingsAppearanceAndLayout),
            subtitle = stringResource(Res.string.settingsAppearanceAndLayoutSubtitle),
            icon = RsserIcons.Appearance,
            onClick = openAppearanceSettings,
          )
        }

        item {
          SettingsNavigationItem(
            title = stringResource(Res.string.settingsFeaturesAndBehaviors),
            subtitle = stringResource(Res.string.settingsFeaturesAndBehaviorsSubtitle),
            icon = RsserIcons.Behaviors,
            onClick = openBehaviorSettings,
          )
        }

        item {
          SettingsNavigationItem(
            title = stringResource(Res.string.settingsServicesAndSync),
            subtitle = stringResource(Res.string.settingsServicesAndSyncSubtitle),
            icon = RsserIcons.Sync,
            onClick = openServicesSettings,
          )
        }

        item {
          SettingsNavigationItem(
            title = stringResource(Res.string.settingsYourInsights),
            subtitle = stringResource(Res.string.settingsYourInsightsSubtitle),
            icon = RsserIcons.Account,
            onClick = openDataSettings,
          )
        }

        item {
          SettingsNavigationItem(
            title = stringResource(Res.string.settingsAppInfoAndFeedback),
            subtitle = null,
            icon = RsserIcons.Changelog,
            onClick = openAppInfoSettings,
          )
        }
      }
    },
    containerColor = AppTheme.colorScheme.backdrop,
    contentColor = Color.Unspecified,
  )
}

@Preview(locale = "en")
@Composable
private fun SettingsPreview() {
  AppTheme {
    SettingsContent(
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
      goBack = {},
      openAppearanceSettings = {},
      openBehaviorSettings = {},
      openServicesSettings = {},
      openDataSettings = {},
      openAppInfoSettings = {},
    )
  }
}
