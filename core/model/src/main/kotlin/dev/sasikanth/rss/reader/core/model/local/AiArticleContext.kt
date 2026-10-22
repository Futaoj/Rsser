package dev.sasikanth.rss.reader.core.model.local

import kotlin.time.Instant

data class AiArticleContext(
  val feedName: String,
  val title: String,
  val summary: String,
  val date: Instant,
  val link: String,
)
