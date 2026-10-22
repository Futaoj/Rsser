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

package dev.sasikanth.rss.reader.resources.icons

import androidx.compose.ui.graphics.vector.ImageVector

val TwineIcons.Website: ImageVector
  get() = rsserWebsite

private val rsserWebsite by lazy {
  rsserIcon(
    "Website",
    "M21 12A9 9 0 1 1 3 12A9 9 0 1 1 21 12ZM3 12H21M12 3C7 8 7 16 12 21C17 16 17 8 12 3",
  )
}
