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

val TwineIcons.DeleteOutline: ImageVector
  get() = rsserDeleteOutline
private val rsserDeleteOutline by lazy {
  rsserIcon("DeleteOutline", "M4 6H20M9 6V3H15V6M6 6L7 21H17L18 6M10 10V17M14 10V17", false)
}
