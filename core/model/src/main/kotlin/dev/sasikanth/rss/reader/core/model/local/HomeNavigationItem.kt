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

package dev.sasikanth.rss.reader.core.model.local

enum class HomeNavigationItem {
  Home,
  Subscriptions,
  Search,
  Discovery,
  Bookmarks,
  Settings,
  AiChat;

  companion object {
    val DEFAULT: List<HomeNavigationItem> =
      listOf(Home, Subscriptions, Discovery, Bookmarks)

    const val SLOT_COUNT = 4

    fun fromPreference(value: String?): List<HomeNavigationItem> {
      if (value.isNullOrBlank()) return DEFAULT

      val items =
        value.split(',').mapNotNull { storedName ->
          entries.firstOrNull { it.name == storedName }
        }

      return if (items.size == SLOT_COUNT && items.distinct().size == SLOT_COUNT) items else DEFAULT
    }
  }
}
