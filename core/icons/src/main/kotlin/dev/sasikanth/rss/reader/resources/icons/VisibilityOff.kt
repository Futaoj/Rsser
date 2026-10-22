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

val TwineIcons.VisibilityOff: ImageVector
  get() = rsserVisibilityOff
private val rsserVisibilityOff by lazy {
  rsserIcon(
    "VisibilityOff",
    "M3 3L21 21M10 5C15 4 19 7 22 12L19 16M6 6L2 12C6 19 11 21 17 18M10 10A3 3 0 0 0 14 14",
  )
}
