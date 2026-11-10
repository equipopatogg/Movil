package com.pactogg.app.core.push

import com.pactogg.app.testing.SecurityChecks
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.experimental.categories.Category

@Category(SecurityChecks::class)
class PushRegistrationTest {
    private val first = "synthetic-first-token-1234567890"
    private val second = "synthetic-second-token-1234567890"

    @Test fun DEV_01_requiresSessionPermissionAndValidToken() = runBlocking {
        var owner: String? = null
        val calls = mutableListOf<String>()
        val registration = PushRegistration({ owner }, { _, token -> calls += token }, { _, _ -> })
        registration.sync(first, true)
        owner = "one"
        registration.sync(first, false)
        registration.sync("short", true)
        registration.sync("synthetic token with whitespace", true)
        registration.sync("x".repeat(4097), true)
        assertTrue(calls.isEmpty())
        registration.sync(first, true)
        assertEquals(listOf(first), calls)
    }

    @Test fun DEV_01_deduplicatesAndRemovesOldTokenBeforeRotation() = runBlocking {
        val calls = mutableListOf<String>()
        val registration = PushRegistration({ "one" }, { _, token -> calls += "put:$token" },
            { _, token -> calls += "delete:$token" })
        registration.sync(first, true)
        registration.sync(first, true)
        registration.sync(second, true)
        assertEquals(listOf("put:$first", "delete:$first", "put:$second"), calls)
    }

    @Test fun DEV_01_registersAgainWhenAccountChanges() = runBlocking {
        var owner = "one"
        val calls = mutableListOf<String>()
        val registration = PushRegistration({ owner }, { id, _ -> calls += id }, { _, _ -> })
        registration.sync(first, true)
        owner = "two"
        registration.sync(first, true)
        assertEquals(listOf("one", "two"), calls)
    }

    @Test fun DEV_02_detachBlocksLateRefreshUntilNextSession() = runBlocking {
        val calls = mutableListOf<String>()
        val registration = PushRegistration({ "one" }, { _, _ -> calls += "put" }, { _, _ -> calls += "delete" })
        registration.sync(first, true)
        assertTrue(registration.detach(null))
        registration.sync(second, true)
        assertEquals(listOf("put", "delete"), calls)
        registration.allowSession()
        registration.sync(second, true)
        assertEquals(listOf("put", "delete", "put"), calls)
    }

    @Test fun DEV_02_failedRemovalDoesNotPermitLateRegistration() = runBlocking {
        var writes = 0
        val registration = PushRegistration({ "one" }, { _, _ -> writes++ }, { _, _ -> error("offline") })
        registration.sync(first, true)
        assertTrue(runCatching { registration.detach(first) }.isFailure)
        registration.sync(second, true)
        assertEquals(1, writes)
    }

    @Test fun DEV_01_failedRegistrationCanBeRetriedOnExplicitEvent() = runBlocking {
        var writes = 0
        val registration = PushRegistration({ "one" }, { _, _ ->
            writes++
            if (writes == 1) error("offline")
        }, { _, _ -> })
        assertTrue(runCatching { registration.sync(first, true) }.isFailure)
        registration.sync(first, true)
        assertEquals(2, writes)
    }

    @Test fun DEV_02_waitsForInflightRegistrationBeforeRemoval() = runBlocking {
        val entered = CompletableDeferred<Unit>()
        val release = CompletableDeferred<Unit>()
        val calls = mutableListOf<String>()
        val registration = PushRegistration({ "one" }, { _, _ ->
            entered.complete(Unit)
            release.await()
            calls += "put"
        }, { _, _ -> calls += "delete" })
        val sync = async { registration.sync(first, true) }
        entered.await()
        val detach = async { registration.detach(first) }
        release.complete(Unit)
        sync.await()
        detach.await()
        assertEquals(listOf("put", "delete"), calls)
    }

    @Test fun DEV_01_unknownPushTypesCannotTriggerNotification() {
        assertFalse(knownPushType(null))
        assertFalse(knownPushType("https://example.com"))
        assertFalse(knownPushType("arbitrary_action"))
        assertTrue(knownPushType("vote_requested"))
        assertTrue(knownPushType("commitment_completed"))
    }

    @Test fun DEV_02_unknownTokenCannotClaimConfirmedRemoteRemoval() = runBlocking {
        val registration = PushRegistration({ "one" }, { _, _ -> }, { _, _ -> error("must not delete all devices") })
        assertFalse(registration.detach(null))
    }

    @Test fun DEV_01_statusNeverClaimsRegistrationWhileRequestIsPending() = runBlocking {
        val entered = CompletableDeferred<Unit>()
        val release = CompletableDeferred<Unit>()
        val states = mutableListOf<PushConnectionState>()
        val registration = PushRegistration({ "one" }, { _, _ ->
            entered.complete(Unit)
            release.await()
        }, { _, _ -> }, states::add)
        val sync = async { registration.sync(first, true) }
        entered.await()
        assertEquals(listOf(PushConnectionState.REGISTERING), states)
        release.complete(Unit)
        sync.await()
        assertEquals(listOf(PushConnectionState.REGISTERING, PushConnectionState.REGISTERED), states)
    }

    @Test fun DEV_01_statusShowsServerFailureAndRequiresConfirmedRetry() = runBlocking {
        val states = mutableListOf<PushConnectionState>()
        var attempts = 0
        val registration = PushRegistration({ "one" }, { _, _ ->
            attempts++
            if (attempts == 1) error("private-provider-detail")
        }, { _, _ -> }, states::add)
        assertTrue(runCatching { registration.sync(first, true) }.isFailure)
        assertEquals(listOf(PushConnectionState.REGISTERING, PushConnectionState.SERVER_ERROR), states)
        registration.sync(first, true)
        assertEquals(PushConnectionState.REGISTERED, states.last())
        assertEquals(2, attempts)
    }

    @Test fun DEV_01_changedAccountCannotReceiveStaleRegistrationConfirmation() = runBlocking {
        var owner = "one"
        val states = mutableListOf<PushConnectionState>()
        val registration = PushRegistration({ owner }, { _, _ -> owner = "two" }, { _, _ -> }, states::add)
        registration.sync(first, true)
        assertFalse(states.contains(PushConnectionState.REGISTERED))
    }
}
