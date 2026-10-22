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

val TwineIcons.Palette: ImageVector
  get() = rsserPalette
private val rsserPalette by lazy {
  rsserIcon(
    "Palette",
    "M21 11C21 2 3 0 3 11C3 18 10 22 14 20C17 18 12 17 15 14C18 12 21 15 21 11ZM7 9H7.1M11 6H11.1M16 7H16.1",
  )
}
