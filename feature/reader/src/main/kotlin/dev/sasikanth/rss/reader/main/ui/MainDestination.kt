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

package dev.sasikanth.rss.reader.main.ui

import androidx.compose.ui.graphics.vector.ImageVector
import dev.sasikanth.rss.reader.core.model.local.HomeNavigationItem
import dev.sasikanth.rss.reader.resources.icons.AiChat
import dev.sasikanth.rss.reader.resources.icons.Bookmark
import dev.sasikanth.rss.reader.resources.icons.BookmarkFilled
import dev.sasikanth.rss.reader.resources.icons.Home
import dev.sasikanth.rss.reader.resources.icons.HomeFilled
import dev.sasikanth.rss.reader.resources.icons.Newsstand
import dev.sasikanth.rss.reader.resources.icons.RSS
import dev.sasikanth.rss.reader.resources.icons.Search
import dev.sasikanth.rss.reader.resources.icons.Settings
import dev.sasikanth.rss.reader.resources.icons.SettingsFilled
import dev.sasikanth.rss.reader.resources.icons.RsserIcons
import org.jetbrains.compose.resources.StringResource
import rsser.shared.generated.resources.Res
import rsser.shared.generated.resources.bookmarks
import rsser.shared.generated.resources.aiChatTitle
import rsser.shared.generated.resources.discoveryTitle
import rsser.shared.generated.resources.feeds
import rsser.shared.generated.resources.mainTabBookmarks
import rsser.shared.generated.resources.mainTabReading
import rsser.shared.generated.resources.mainTabSubscriptions
import rsser.shared.generated.resources.postsSearchHint
import rsser.shared.generated.resources.screenHome
import rsser.shared.generated.resources.settings

internal enum class MainDestination(
  val navigationItem: HomeNavigationItem,
  val icon: ImageVector,
  val selectedIcon: ImageVector,
  val label: StringResource,
  val bottomBarLabel: StringResource,
) {
  Home(
    navigationItem = HomeNavigationItem.Home,
    icon = RsserIcons.Home,
    selectedIcon = RsserIcons.HomeFilled,
    label = Res.string.screenHome,
    bottomBarLabel = Res.string.mainTabReading,
  ),
  Subscriptions(
    navigationItem = HomeNavigationItem.Subscriptions,
    icon = RsserIcons.RSS,
    selectedIcon = RsserIcons.RSS,
    label = Res.string.feeds,
    bottomBarLabel = Res.string.mainTabSubscriptions,
  ),
  Search(
    navigationItem = HomeNavigationItem.Search,
    icon = RsserIcons.Search,
    selectedIcon = RsserIcons.Search,
    label = Res.string.postsSearchHint,
    bottomBarLabel = Res.string.postsSearchHint,
  ),
  Discovery(
    navigationItem = HomeNavigationItem.Discovery,
    icon = RsserIcons.Newsstand,
    selectedIcon = RsserIcons.Newsstand,
    label = Res.string.discoveryTitle,
    bottomBarLabel = Res.string.discoveryTitle,
  ),
  AiChat(
    navigationItem = HomeNavigationItem.AiChat,
    icon = RsserIcons.AiChat,
    selectedIcon = RsserIcons.AiChat,
    label = Res.string.aiChatTitle,
    bottomBarLabel = Res.string.aiChatTitle,
  ),
  Bookmarks(
    navigationItem = HomeNavigationItem.Bookmarks,
    icon = RsserIcons.Bookmark,
    selectedIcon = RsserIcons.BookmarkFilled,
    label = Res.string.bookmarks,
    bottomBarLabel = Res.string.mainTabBookmarks,
  ),
  Settings(
    navigationItem = HomeNavigationItem.Settings,
    icon = RsserIcons.Settings,
    selectedIcon = RsserIcons.SettingsFilled,
    label = Res.string.settings,
    bottomBarLabel = Res.string.settings,
  ),
}
