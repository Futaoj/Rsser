package dev.sasikanth.rss.reader.ai

import kotlinx.serialization.Serializable

@Serializable
data class DeepSeekRequest(
  val model: String = DeepSeekClient.MODEL,
  val messages: List<DeepSeekMessage>,
  val stream: Boolean = false,
)

@Serializable
data class DeepSeekMessage(val role: String, val content: String)

@Serializable
data class DeepSeekResponse(val choices: List<DeepSeekChoice> = emptyList())

@Serializable
data class DeepSeekChoice(val message: DeepSeekMessage)
