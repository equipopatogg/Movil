package com.pactogg.app.core.push

import com.pactogg.app.core.network.PactoApi
import com.pactogg.app.core.session.SessionManager
import com.pactogg.app.feature.auth.domain.Account
import com.pactogg.app.feature.auth.domain.Session
import com.pactogg.app.feature.auth.domain.SessionStore
import com.pactogg.app.testing.SecurityChecks
import java.lang.reflect.Proxy
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.experimental.categories.Category
import retrofit2.Response

@Category(SecurityChecks::class)
class PushSessionTest {
    @Test fun DEV_02_deviceHookRunsBeforeSessionClearingAndRemoteLogout() = runBlocking {
        val calls = mutableListOf<String>()
        val store = PushStore()
        val manager = sessions(store, calls)
        manager.restore()
        manager.beforeLogout = {
            assertNotNull(manager.current.value)
            calls += "device"
            true
        }
        assertTrue(manager.logout())
        assertEquals(listOf("device", "logout"), calls)
        assertNull(store.saved)
        assertNull(manager.current.value)
    }

    @Test fun DEV_02_deviceHookFailureCannotPreventLocalLogout() = runBlocking {
        val calls = mutableListOf<String>()
        val store = PushStore()
        val manager = sessions(store, calls)
        manager.restore()
        manager.beforeLogout = { error("offline") }
        assertFalse(manager.logout())
        assertEquals(listOf("logout"), calls)
        assertNull(store.saved)
        assertNull(manager.current.value)
    }

    @Test fun DEV_02_withoutFirebaseExistingLogoutRemainsAvailable() = runBlocking {
        val calls = mutableListOf<String>()
        val manager = sessions(PushStore(), calls)
        manager.restore()
        assertTrue(manager.logout())
        assertEquals(listOf("logout"), calls)
    }

    private fun sessions(store: SessionStore, calls: MutableList<String>): SessionManager {
        val api = Proxy.newProxyInstance(PactoApi::class.java.classLoader, arrayOf(PactoApi::class.java)) {
            _, method, _ ->
            check(method.name == "logout")
            calls += "logout"
            Response.success<Unit>(204, null)
        } as PactoApi
        return SessionManager(api, store) { 1000L }
    }
}

private class PushStore : SessionStore {
    var saved: Session? = Session(Account("one", "qa@example.com", "Player"),
        "synthetic-access", "synthetic-refresh", 100000L)
    override fun read() = saved
    override fun write(session: Session) { saved = session }
    override fun clear() { saved = null }
}
