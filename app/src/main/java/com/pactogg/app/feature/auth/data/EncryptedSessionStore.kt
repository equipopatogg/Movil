package com.pactogg.app.feature.auth.data

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.AtomicFile
import com.pactogg.app.feature.auth.domain.Session
import com.pactogg.app.feature.auth.domain.SessionStore
import java.io.File
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/** Private no-backup file. Tokens are encrypted together and committed atomically. */
class EncryptedSessionStore(context: Context, private val alias: String = "pactogg.session.v1") : SessionStore {
    private val file = AtomicFile(File(context.noBackupFilesDir, "session.enc"))

    @Synchronized
    override fun read(): Session? {
        if (!file.baseFile.exists()) return null
        return runCatching {
            require(file.baseFile.length() in 29..65536)
            val bytes = file.readFully()
            require(bytes.size in 29..65536 && bytes[0] == 1.toByte())
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, key(), GCMParameterSpec(128, bytes.copyOfRange(1, 13)))
            val stored = Json.decodeFromString<StoredSession>(
                cipher.doFinal(bytes.copyOfRange(13, bytes.size)).toString(Charsets.UTF_8))
            require(stored.expiresAt > 0)
            stored.session.toDomain(0).let {
                Session(it.account, it.accessToken, it.refreshToken, stored.expiresAt)
            }
        }.getOrElse {
            clear()
            null
        }
    }

    @Synchronized
    override fun write(session: Session) {
        val dto = SessionDto(UserDto(session.account.id, session.account.email, session.account.nickname),
            session.accessToken, session.refreshToken, "Bearer", 900)
        val plain = Json.encodeToString(StoredSession(dto, session.expiresAtMillis)).toByteArray()
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key())
        val encrypted = byteArrayOf(1) + cipher.iv + cipher.doFinal(plain)
        plain.fill(0)
        val stream = file.startWrite()
        var committed = false
        try {
            stream.write(encrypted)
            file.finishWrite(stream)
            committed = true
        } finally {
            if (!committed) file.failWrite(stream)
        }
    }

    @Synchronized
    override fun clear() { file.delete() }

    private fun key(): SecretKey {
        val store = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        (store.getKey(alias, null) as? SecretKey)?.let { return it }
        return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore").apply {
            init(KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setRandomizedEncryptionRequired(true).build())
        }.generateKey()
    }
}

@Serializable
private class StoredSession(val session: SessionDto, val expiresAt: Long)
