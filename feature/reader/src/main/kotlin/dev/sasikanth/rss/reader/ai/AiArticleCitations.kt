package dev.sasikanth.rss.reader.ai

import dev.sasikanth.rss.reader.core.model.local.AiArticleContext

internal data class AiArticleCitationResult(
  val content: String,
  val citations: List<AiArticleCitation>,
)

private val articleCitationMarker = Regex("【(\\d+)】")

internal fun resolveArticleCitations(
  answer: String,
  articles: List<AiArticleContext>,
): AiArticleCitationResult {
  val citations = linkedMapOf<String, AiArticleCitation>()
  val content =
    articleCitationMarker.replace(answer) { match ->
      val articleIndex = match.groupValues[1].toIntOrNull()?.minus(1)
      val article = articleIndex?.let(articles::getOrNull)
      if (article == null) {
        "〔来源不可用〕"
      } else {
        citations.putIfAbsent(
          article.id,
          AiArticleCitation(
            postId = article.id,
            title = article.title,
            feedName = article.feedName,
            imageUrl = article.imageUrl,
          ),
        )
        "〔${articleIndex + 1}〕"
      }
    }

  return AiArticleCitationResult(content = content, citations = citations.values.toList())
}
