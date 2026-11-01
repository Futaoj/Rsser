package dev.sasikanth.rss.reader.ai

import kotlin.test.Test
import kotlin.test.assertEquals

class DeepSeekFailureClassifierTest {
  @Test
  fun `maps authentication failure to invalid key`() {
    assertEquals(DeepSeekException.InvalidKey, classifyDeepSeekFailure(401, "{}"))
  }

  @Test
  fun `maps insufficient balance to quota error`() {
    assertEquals(
      DeepSeekException.InsufficientCredits,
      classifyDeepSeekFailure(402, """{"error":{"code":"insufficient_balance"}}"""),
    )
  }

  @Test
  fun `maps invalid request to request error instead of network error`() {
    assertEquals(
      DeepSeekException.InvalidRequest,
      classifyDeepSeekFailure(400, """{"error":{"type":"invalid_request_error"}}"""),
    )
  }

  @Test
  fun `maps rate limiting separately`() {
    assertEquals(DeepSeekException.RateLimited, classifyDeepSeekFailure(429, "{}"))
  }

  @Test
  fun `maps server failures separately`() {
    assertEquals(DeepSeekException.ServiceUnavailable, classifyDeepSeekFailure(503, "{}"))
  }
}
