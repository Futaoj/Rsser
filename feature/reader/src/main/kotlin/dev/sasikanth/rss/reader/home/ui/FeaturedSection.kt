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
package dev.sasikanth.rss.reader.home.ui

import androidx.compose.animation.core.EaseInSine
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onVisibilityChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import dev.sasikanth.rss.reader.components.HorizontalPageIndicators
import dev.sasikanth.rss.reader.components.PageIndicatorState
import dev.sasikanth.rss.reader.core.model.local.FeaturedPostItem
import dev.sasikanth.rss.reader.core.model.local.PostsType
import dev.sasikanth.rss.reader.core.model.local.ResolvedPost
import dev.sasikanth.rss.reader.data.repository.MarkAsReadOn
import dev.sasikanth.rss.reader.utils.LocalWindowSizeClass
import dev.sasikanth.rss.reader.utils.ParallaxAlignment
import dev.sasikanth.rss.reader.utils.getOffsetFractionForPage
import dev.sasikanth.rss.reader.utils.inverse
import kotlin.math.absoluteValue
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun FeaturedSection(
  paddingValues: PaddingValues,
  featuredPosts: ImmutableList<FeaturedPostItem>,
  pagerState: PagerState,
  postsType: PostsType,
  markAsReadOn: MarkAsReadOn,
  modifier: Modifier = Modifier,
  markFeaturedPostAsReadOnScroll: (String) -> Unit,
  onItemClick: (ResolvedPost, postIndex: Int) -> Unit,
  onPostBookmarkClick: (ResolvedPost) -> Unit,
  onPostCommentsClick: (String) -> Unit,
  onPostSourceClick: (String) -> Unit,
  updateReadStatus: (id: String, updatedReadStatus: Boolean) -> Unit,
) {
  val layoutDirection = LocalLayoutDirection.current
  val density = LocalDensity.current
  val sizeClass = LocalWindowSizeClass.current

  val systemBars = WindowInsets.systemBars
  val systemBarsPaddingValues =
    remember(systemBars, density) {
      systemBars.only(WindowInsetsSides.Horizontal).asPaddingValues(density)
    }
  val systemBarsHorizontalPadding =
    remember(systemBarsPaddingValues, layoutDirection) {
      val systemBarsStartPadding = systemBarsPaddingValues.calculateStartPadding(layoutDirection)
      val systemBarsEndPadding = systemBarsPaddingValues.calculateEndPadding(layoutDirection)

      if (systemBarsStartPadding > systemBarsEndPadding) {
        systemBarsStartPadding
      } else {
        systemBarsEndPadding
      }
    }

  Box(modifier) {
    val contentPadding =
      remember(systemBarsHorizontalPadding, featuredPosts.isNotEmpty()) {
        PaddingValues(
          start = systemBarsHorizontalPadding + 16.dp,
          end = systemBarsHorizontalPadding + 16.dp,
          bottom = if (featuredPosts.isNotEmpty()) 24.dp else 0.dp,
        )
      }

    val featuredPostKey =
      remember(featuredPosts) {
        { page: Int ->
          featuredPosts.getOrNull(page)?.let { PostListKey.encode(it.resolvedPost) } ?: page
        }
      }

    HorizontalPager(
      state = pagerState,
      verticalAlignment = Alignment.Top,
      contentPadding = contentPadding,
      beyondViewportPageCount = 1,
      key = featuredPostKey,
    ) { page ->
      val featuredPost = featuredPosts.getOrNull(page)
      if (featuredPost != null) {
        val postWithMetadata = featuredPost.resolvedPost
        val imageAlignment =
          remember(page) {
            ParallaxAlignment(
              horizontalBias = { pagerState.getOffsetFractionForPage(page) },
              multiplier = 2f,
            )
          }

        val contentAlphaProvider =
          remember(page) {
            {
              val pageOffset = pagerState.getOffsetFractionForPage(page)
              calculateContentAlpha(pageOffset)
            }
          }

        Box(
          modifier =
            Modifier.onVisibilityChanged(minDurationMs = 500) {
              val previousFeaturedPost =
                featuredPosts.getOrNull(page - 1) ?: return@onVisibilityChanged

              if (previousFeaturedPost.resolvedPost.read) return@onVisibilityChanged

              if (it) {
                markFeaturedPostAsReadOnScroll(previousFeaturedPost.resolvedPost.id)
              }
            }
        ) {
          FeaturedPostItem(
            item = postWithMetadata,
            contentAlphaProvider = contentAlphaProvider,
            onClick = { onItemClick(postWithMetadata, page) },
            onBookmarkClick = { onPostBookmarkClick(postWithMetadata) },
            onCommentsClick = { onPostCommentsClick(postWithMetadata.commentsLink!!) },
            onSourceClick = { onPostSourceClick(postWithMetadata.sourceId) },
            updateReadStatus = { updatedReadStatus ->
              updateReadStatus(postWithMetadata.id, updatedReadStatus)
            },
            modifier =
              Modifier.padding(top = paddingValues.calculateTopPadding())
                .padding(horizontal = 6.dp),
            { FeaturedImage(imageUrl = postWithMetadata.imageUrl, alignment = imageAlignment) },
          )
        }
      }
    }

    if (pagerState.pageCount > 1) {
      val pageIndicatorState = remember {
        object : PageIndicatorState {
          override val pageOffset: Float
            get() = pagerState.currentPageOffsetFraction

          override val selectedPage: Int
            get() = pagerState.currentPage

          override val pageCount: Int
            get() = pagerState.pageCount
        }
      }

      HorizontalPageIndicators(
        modifier = Modifier.padding(vertical = 8.dp).align(Alignment.BottomCenter),
        pageIndicatorState = pageIndicatorState,
      )
    }
  }
}

private fun calculateContentAlpha(pageOffset: Float): Float {
  val offsetAbsolute = minOf(1f, pageOffset.absoluteValue)
  return EaseInSine.transform(offsetAbsolute.inverse())
}
