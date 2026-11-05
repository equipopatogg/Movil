package com.pactogg.app.feature.connected.presentation


import org.junit.experimental.categories.Category
import com.pactogg.app.testing.UnitChecks
import com.pactogg.app.core.network.ApiClientFactory
import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.core.network.PactoApi
import com.pactogg.app.core.session.SessionManager
import com.pactogg.app.feature.auth.domain.Session
import com.pactogg.app.feature.auth.domain.SessionStore
import com.pactogg.app.feature.connected.data.ApiProductRepository
import com.pactogg.app.feature.connected.domain.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@Category(UnitChecks::class)
class NicknameQueriesTest {
    private val dispatcher = StandardTestDispatcher()
    private lateinit var model: ConnectedViewModel
    private val social = FakeSocial()
    @Before fun setup() {
        Dispatchers.setMain(dispatcher)
        val api = ApiClientFactory().create("https://localhost/").create(PactoApi::class.java)
        val store = object : SessionStore {
            override fun read(): Session? = null
            override fun write(session: Session) = Unit
            override fun clear() = Unit
        }
        val sessions = SessionManager(api, store)
        model = ConnectedViewModel(sessions, ApiProductRepository(api, sessions), social)
    }
    @After fun cleanup() { Dispatchers.resetMain() }
    @Test fun typingWaitsAndInvalidNameNeverQueries() = runTest(dispatcher) {
        runCurrent()
        model.checkNickname("a")
        advanceTimeBy(1000); runCurrent()
        assertEquals(0, social.calls)
        model.checkNickname("ValidName")
        advanceTimeBy(699); runCurrent()
        assertEquals(0, social.calls)
        advanceTimeBy(2); runCurrent()
        assertEquals(1, social.calls)
        assertTrue(model.state.value.social.nicknameNotice.orEmpty().contains("disponible"))
    }
    @Test fun cancelledSlowResponseCannotOverwriteNewNameResult() = runTest(dispatcher) {
        runCurrent()
        val first = CompletableDeferred<NicknameAvailability>()
        social.check = { name ->
            if (name == "FirstName") withContext(NonCancellable) { first.await() }
            else NicknameAvailability(name, false)
        }
        model.checkNickname("FirstName")
        advanceTimeBy(701); runCurrent()
        model.checkNickname("SecondName")
        advanceTimeBy(701); runCurrent()
        assertTrue(model.state.value.social.nicknameNotice.orEmpty().contains("ocupado"))
        first.complete(NicknameAvailability("FirstName", true)); runCurrent()
        assertTrue(model.state.value.social.nicknameNotice.orEmpty().contains("ocupado"))
    }
    @Test fun rateLimitAddsCooldownWithoutPolling() = runTest(dispatcher) {
        runCurrent()
        social.check = { throw ApiFailure(429, "RATE_LIMITED") }
        model.checkNickname("FirstName")
        advanceTimeBy(701); runCurrent()
        model.checkNickname("SecondName")
        advanceTimeBy(701); runCurrent()
        assertEquals(1, social.calls)
        assertTrue(model.state.value.social.nicknameNotice.orEmpty().contains("Espera"))
    }
    private class FakeSocial : SocialRepository {
        var calls = 0
        var check: suspend (String) -> NicknameAvailability = { NicknameAvailability(it, true) }
        override suspend fun nickname(value: String): NicknameAvailability { calls++; return check(value) }
        override suspend fun profile(): Profile = error("unexpected")
        override suspend fun updateProfile(nickname: String): Profile = error("unexpected")
        override suspend fun groups(query: String, cursor: String?): Page<Group> = error("unexpected")
        override suspend fun group(id: String): Group = error("unexpected")
        override suspend fun join(code: String): Group = error("unexpected")
        override suspend fun members(id: String, query: String, cursor: String?): Page<Member> = error("unexpected")
        override suspend fun contracts(id: String, filter: BoardFilter, cursor: String?): Page<Challenge> = error("unexpected")
        override suspend fun accept(contract: Challenge): Challenge = error("unexpected")
        override suspend fun reject(contract: Challenge): Challenge = error("unexpected")
    }

    @Test fun elapsedCooldownAllowsNewQueryAndEditingWithoutAutomaticPolling() = runTest(dispatcher) {
        runCurrent(); social.check = { throw ApiFailure(429, "RATE_LIMITED") }
        model.checkNickname("FirstName"); advanceTimeBy(701); runCurrent()
        val retryAt = model.nicknameRetryAt
        assertTrue(retryAt > System.currentTimeMillis())
        model.editNickname("SecondName"); advanceTimeBy(701); runCurrent()
        assertEquals("SecondName", model.state.value.social.editedNickname); assertEquals(1, social.calls)
        // Arrange the real-clock deadline in the past; virtual coroutine time alone cannot expire it.
        model.nicknameRetryAt = System.currentTimeMillis() - 1
        social.check = { NicknameAvailability(it, true) }
        model.editNickname("ThirdName"); advanceTimeBy(701); runCurrent()
        assertEquals(2, social.calls)
        assertTrue(model.state.value.social.nicknameNotice.orEmpty().contains("disponible"))
        advanceTimeBy(120000); runCurrent(); assertEquals(2, social.calls)
    }

}
