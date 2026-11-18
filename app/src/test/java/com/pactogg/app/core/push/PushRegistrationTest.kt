package com.pactogg.app.core.push

import com.pactogg.app.testing.UnitChecks
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.experimental.categories.Category

/** PL-10: registro, rotación y baja del token sin red real. */
@Category(UnitChecks::class)
class PushRegistrationTest {
    private val first = "a".repeat(40)
    private val second = "b".repeat(40)
    private var owner: String? = "user-1"
    private val calls = mutableListOf<String>()
    private val states = mutableListOf<PushConnectionState>()
    private val registration = PushRegistration({ owner },
        register = { who, token -> calls += "put:$who:${token.take(1)}" },
        remove = { who, token -> calls += "delete:$who:${token.take(1)}" },
        stateChanged = { states += it })

    @Test fun PL_10_registersOnceAndRotationRemovesTheOldToken() = runBlocking {
        registration.sync(first, true)
        registration.sync(first, true)
        registration.sync(second, true)
        assertEquals(listOf("put:user-1:a", "delete:user-1:a", "put:user-1:b"), calls)
        assertEquals(PushConnectionState.REGISTERED, states.last())
    }

    @Test fun PL_10_noPermissionInvalidTokenOrChangedAccountRegistersNothing() = runBlocking {
        registration.sync(first, false)
        registration.sync("short", true)
        registration.sync("x".repeat(30) + " " + "y".repeat(10), true)
        owner = null
        registration.sync(first, true)
        assertTrue(calls.isEmpty())
    }

    @Test fun PL_10_detachRemovesRegisteredTokenAndBlocksLateRegistration() = runBlocking {
        registration.sync(first, true)
        assertTrue(registration.detach(null))
        registration.sync(second, true)
        assertEquals(listOf("put:user-1:a", "delete:user-1:a"), calls)
        registration.allowSession()
        registration.sync(second, true)
        assertEquals("put:user-1:b", calls.last())
    }

    @Test fun PL_10_serverFailureReportsErrorAndKeepsNoRegistration() = runBlocking {
        val failing = PushRegistration({ "user-1" }, register = { _, _ -> error("offline") },
            remove = { _, _ -> }, stateChanged = { states += it })
        runCatching { failing.sync(first, true) }
        assertEquals(PushConnectionState.SERVER_ERROR, states.last())
    }
}
