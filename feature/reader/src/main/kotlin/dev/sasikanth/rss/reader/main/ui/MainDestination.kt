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
import rsser.shared.generated.resources.discoveryTitle
import rsser.shared.generated.resources.feeds
import rsser.shared.generated.resources.postsSearchHint
import rsser.shared.generated.resources.screenHome
import rsser.shared.generated.resources.settings

internal enum class MainDestination(
  val icon: ImageVector,
  val selectedIcon: ImageVector,
  val label: StringResource,
) {
  Home(icon = RsserIcons.Home, selectedIcon = RsserIcons.HomeFilled, label = Res.string.screenHome),
  Subscriptions(
    icon = RsserIcons.RSS,
    selectedIcon = RsserIcons.RSS,
    label = Res.string.feeds,
  ),
  Search(
    icon = RsserIcons.Search,
    selectedIcon = RsserIcons.Search,
    label = Res.string.postsSearchHint,
  ),
  Discovery(
    icon = RsserIcons.Newsstand,
    selectedIcon = RsserIcons.Newsstand,
    label = Res.string.discoveryTitle,
  ),
  Bookmarks(
    icon = RsserIcons.Bookmark,
    selectedIcon = RsserIcons.BookmarkFilled,
    label = Res.string.bookmarks,
  ),
  Settings(
    icon = RsserIcons.Settings,
    selectedIcon = RsserIcons.SettingsFilled,
    label = Res.string.settings,
  ),
}
