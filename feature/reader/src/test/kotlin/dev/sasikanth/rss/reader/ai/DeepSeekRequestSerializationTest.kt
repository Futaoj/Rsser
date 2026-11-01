package dev.sasikanth.rss.reader.ai

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertContains

class DeepSeekRequestSerializationTest {
  @Test
  fun `request includes required model and stream parameters`() {
    val json = Json { ignoreUnknownKeys = true }
    val encoded =
      json.encodeToString(
        DeepSeekRequest(
          model = DeepSeekClient.MODEL,
          messages = listOf(DeepSeekMessage("user", "hello")),
          stream = false,
        )
      )

    assertContains(encoded, "\"model\":\"deepseek-flash\"")
    assertContains(encoded, "\"stream\":false")
  }
}
