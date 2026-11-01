package dev.sasikanth.rss.reader.ai

import dev.sasikanth.rss.reader.core.model.local.AiArticleContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class AiArticleCitationsTest {
  @Test
  fun `resolves citation markers to unique article cards`() {
    val articles = listOf(article())

    val result = resolveArticleCitations("观点【1】；再次引用【1】", articles)

    assertEquals("观点〔1〕；再次引用〔1〕", result.content)
    assertEquals(
      listOf(
        AiArticleCitation(
          postId = "post-1",
          title = "文章标题",
          feedName = "来源",
          imageUrl = "https://example.com/cover.jpg",
        )
      ),
      result.citations,
    )
  }

  @Test
  fun `replaces out of range references without creating a card`() {
    val result = resolveArticleCitations("观点【2】", listOf(article()))

    assertEquals("观点〔来源不可用〕", result.content)
    assertEquals(emptyList(), result.citations)
  }

  private fun article() =
    AiArticleContext(
      id = "post-1",
      feedName = "来源",
      title = "文章标题",
      summary = "摘要",
      date = Instant.parse("2026-10-26T00:00:00Z"),
      link = "https://example.com/article",
      imageUrl = "https://example.com/cover.jpg",
    )
}
