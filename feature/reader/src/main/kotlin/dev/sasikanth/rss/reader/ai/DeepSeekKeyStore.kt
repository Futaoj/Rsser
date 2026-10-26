package dev.sasikanth.rss.reader.ai

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import dev.sasikanth.rss.reader.di.scopes.AppScope
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import me.tatarka.inject.annotations.Inject

@Inject
@AppScope
class DeepSeekKeyStore(private val context: Context) {

  fun read(): String? {
    val encoded = preferences.getString(KEY_VALUE, null) ?: return null
    return runCatching {
      val combined = Base64.decode(encoded, Base64.NO_WRAP)
      val iv = combined.copyOfRange(0, IV_SIZE)
      val encrypted = combined.copyOfRange(IV_SIZE, combined.size)
      Cipher.getInstance(TRANSFORMATION).run {
        init(Cipher.DECRYPT_MODE, secretKey(), GCMParameterSpec(TAG_SIZE, iv))
        String(doFinal(encrypted), Charsets.UTF_8)
      }
    }.getOrNull()
  }

  fun write(value: String) {
    if (value.isBlank()) {
      clear()
      return
    }
    val cipher = Cipher.getInstance(TRANSFORMATION)
    cipher.init(Cipher.ENCRYPT_MODE, secretKey())
    val combined = cipher.iv + cipher.doFinal(value.trim().toByteArray(Charsets.UTF_8))
    preferences.edit().putString(KEY_VALUE, Base64.encodeToString(combined, Base64.NO_WRAP)).apply()
  }

  fun clear() {
    preferences.edit().remove(KEY_VALUE).apply()
  }

  private fun secretKey(): SecretKey {
    val keyStore = KeyStore.getInstance(KEYSTORE).apply { load(null) }
    (keyStore.getKey(KEY_ALIAS, null) as? SecretKey)?.let { return it }
    return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE).run {
      init(
        KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
          )
          .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
          .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
          .setRandomizedEncryptionRequired(true)
          .build()
      )
      generateKey()
    }
  }

  private val preferences by lazy {
    context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
  }

  private companion object {
    const val KEY_ALIAS = "rsser_deepseek_api_key"
    const val KEYSTORE = "AndroidKeyStore"
    const val PREFERENCES_NAME = "deepseek_secure_settings"
    const val KEY_VALUE = "encrypted_api_key"
    const val TRANSFORMATION = "AES/GCM/NoPadding"
    const val IV_SIZE = 12
    const val TAG_SIZE = 128
  }
}
