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

val TwineIcons.RadioSelected: ImageVector
  get() = rsserRadioSelected
private val rsserRadioSelected by lazy {
  rsserIcon(
    "RadioSelected",
    "M21 12A9 9 0 1 1 3 12A9 9 0 1 1 21 12ZM16 12A4 4 0 1 1 8 12A4 4 0 1 1 16 12Z",
  )
}
