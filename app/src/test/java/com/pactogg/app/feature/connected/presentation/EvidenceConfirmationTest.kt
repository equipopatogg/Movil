package com.pactogg.app.feature.connected.presentation

import org.junit.experimental.categories.Category
import com.pactogg.app.testing.UnitChecks
import com.pactogg.app.core.network.ApiClientFactory
import com.pactogg.app.core.network.PactoApi
import com.pactogg.app.core.session.SessionManager
import com.pactogg.app.feature.auth.domain.Account
import com.pactogg.app.feature.auth.domain.Session
import com.pactogg.app.feature.auth.domain.SessionStore
import com.pactogg.app.feature.connected.domain.Challenge
import com.pactogg.app.feature.connected.domain.ChallengeDraft
import com.pactogg.app.feature.connected.domain.Evidence
import com.pactogg.app.feature.connected.domain.Group
import com.pactogg.app.feature.connected.domain.ProductRepository
import com.pactogg.app.feature.connected.domain.TournamentPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@Category(UnitChecks::class)
class EvidenceConfirmationTest {
    private val dispatcher = StandardTestDispatcher()
    private val contract = Challenge("contract", "group", "Halo", "Ganar", "Elegir juego", "2099-01-01T00:00:00Z", 25, "open")
    private val evidence = Evidence("evidence", "contract", "photo", "approved")
    private lateinit var model: ConnectedViewModel
    private val products = FakeProducts()

    @Before fun setup() {
        Dispatchers.setMain(dispatcher)
        val api = ApiClientFactory().create("https://localhost/").create(PactoApi::class.java)
        val store = object : SessionStore {
            override fun read(): Session? = null
            override fun write(session: Session) = Unit
            override fun clear() = Unit
        }
        val sessions = SessionManager(api, store)
        model = ConnectedViewModel(sessions, products,
            com.pactogg.app.feature.connected.data.ApiSocialRepository(api, sessions))
    }

    @After fun cleanup() { Dispatchers.resetMain() }

    @Test fun confirmedUploadRefreshesActualStatusWithoutInventingIt() = runTest(dispatcher) {
        prepare()
        products.read = { contract.copy(status = "voting") }
        model.confirmEvidence(products, contract, evidence, "Foto confirmada.")
        assertEquals("voting", model.state.value.contract?.status)
        assertEquals(evidence, model.state.value.evidence)
        assertFalse(model.state.value.contractStateStale)
        assertEquals(1, products.reads)
    }

    @Test fun failedReadKeepsConfirmedEvidenceAndMarksStatusStaleWithoutAnotherUpload() = runTest(dispatcher) {
        prepare()
        products.read = { error("read unavailable") }
        model.confirmEvidence(products, contract, evidence, "Foto confirmada.")
        assertEquals(evidence, model.state.value.evidence)
        assertTrue(model.state.value.contractStateStale)
        assertTrue(model.state.value.notice.orEmpty().contains("No repitas el envío"))
        assertNull(model.state.value.error)
        assertEquals(1, products.reads)
    }

    @Test fun lateReadCannotRestoreDataAfterSessionWasCleared() = runTest(dispatcher) {
        prepare()
        products.read = {
            model.mutable.value = ConnectedState(restoring = false)
            contract.copy(status = "voting")
        }
        model.confirmEvidence(products, contract, evidence, "Foto confirmada.")
        assertNull(model.state.value.account)
        assertNull(model.state.value.contract)
        assertNull(model.state.value.evidence)
        assertNull(model.state.value.notice)
    }

    private fun kotlinx.coroutines.test.TestScope.prepare() {
        runCurrent()
        model.mutable.value = ConnectedState(restoring = false,
            account = Account("qa", "qa@example.com", "QA"), contract = contract)
    }

    private class FakeProducts : ProductRepository {
        var reads = 0
        var read: suspend () -> Challenge = { error("not configured") }
        override suspend fun challenge(contract: Challenge): Challenge { reads++; return read() }
        override suspend fun createGroup(name: String): Group = error("unexpected write")
        override suspend fun createChallenge(group: Group, draft: ChallengeDraft): Challenge = error("unexpected write")
        override suspend fun sendLink(contract: Challenge, url: String): Evidence = error("unexpected upload")
        override suspend fun sendPhoto(contract: Challenge, bytes: ByteArray, mimeType: String): Evidence = error("unexpected upload")
        override suspend fun tournaments(status: String?, game: String?, cursor: String?): TournamentPage = error("unexpected read")
    }
}
