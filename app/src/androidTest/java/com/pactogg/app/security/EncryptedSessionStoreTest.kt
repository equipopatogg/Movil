package com.pactogg.app.security

import android.content.ContextWrapper
import androidx.test.platform.app.InstrumentationRegistry
import com.pactogg.app.feature.auth.data.EncryptedSessionStore
import com.pactogg.app.feature.auth.domain.Account
import com.pactogg.app.feature.auth.domain.Session
import java.io.File
import java.security.KeyStore
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class EncryptedSessionStoreTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val folder = File(context.noBackupFilesDir, "session-instrumented-test")
    private val alias = "pactogg.session.test-only"
    private lateinit var store: EncryptedSessionStore

    @Before fun setup() {
        folder.mkdirs()
        val isolated = object : ContextWrapper(context) {
            override fun getNoBackupFilesDir(): File = folder
        }
        store = EncryptedSessionStore(isolated, alias)
        store.clear()
    }

    @After fun cleanup() {
        store.clear()
        KeyStore.getInstance("AndroidKeyStore").apply { load(null); deleteEntry(alias) }
        folder.delete()
    }

    @Test fun roundTripIsEncryptedAndClearRemovesTheFile() {
        val session = Session(Account("111111111111111111111111", "sample@example.com", "Player"),
            "synthetic-access", "synthetic-refresh", System.currentTimeMillis() + 900000)
        store.write(session)
        val disk = File(folder, "session.enc").readBytes().toString(Charsets.ISO_8859_1)
        assertFalse(disk.contains("synthetic-access"))
        assertFalse(disk.contains("sample@example.com"))
        assertEquals("synthetic-refresh", store.read()?.refreshToken)
        store.clear()
        assertNull(store.read())
        assertFalse(File(folder, "session.enc").exists())
    }

    @Test fun corruptedOrMissingKeyDoesNotRestoreAnAccount() {
        val session = Session(Account("111111111111111111111111", "sample@example.com", "Player"),
            "synthetic-access", "synthetic-refresh", System.currentTimeMillis() + 900000)
        store.write(session)
        val file = File(folder, "session.enc")
        val bytes = file.readBytes()
        bytes[bytes.lastIndex] = (bytes.last().toInt() xor 1).toByte()
        file.writeBytes(bytes)
        assertNull(store.read())
        store.write(session)
        KeyStore.getInstance("AndroidKeyStore").apply { load(null); deleteEntry(alias) }
        assertNull(store.read())
    }
}
