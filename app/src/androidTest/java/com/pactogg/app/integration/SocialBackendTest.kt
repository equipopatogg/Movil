package com.pactogg.app.integration

import android.os.Bundle
import android.view.View
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.ViewModelProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.pactogg.app.BuildConfig
import com.pactogg.app.MainActivity
import com.pactogg.app.core.network.ApiClientFactory
import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.core.network.PactoApi
import com.pactogg.app.core.session.SessionManager
import com.pactogg.app.feature.auth.data.LoginRequest
import com.pactogg.app.feature.auth.domain.Session
import com.pactogg.app.feature.auth.domain.SessionStore
import com.pactogg.app.feature.connected.data.ApiProductRepository
import com.pactogg.app.feature.connected.data.ApiSocialRepository
import com.pactogg.app.feature.connected.domain.ChallengeDraft
import com.pactogg.app.feature.connected.presentation.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Assume.assumeTrue
import org.junit.Rule
import org.junit.Test

/** Opt-in, two explicitly supplied synthetic accounts; reuses an existing QA group. */
class SocialBackendTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()
    private lateinit var model: ConnectedViewModel

    @Test fun confirmedSocialOperationsOnDevice() = runBlocking {
        val args = InstrumentationRegistry.getArguments()
        assumeTrue(args.getString("pactogg.social.live") == "true")
        compose.runOnIdle {
            model = ViewModelProvider(compose.activity)[ConnectedViewModel::class.java]
            compose.activity.window.decorView.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
        }
        compose.waitUntil(60000) { !model.state.value.restoring }
        check(model.state.value.account?.email == args.getString("qa.email"))
        val owner = requireNotNull(model.state.value.account)
        compose.runOnIdle { model.navigate(ConnectedRoute.PROFILE) }
        awaitOperation()
        assertNotNull(model.state.value.social.profile)
        report("USR-02 profile PASS")
        val original = owner.nickname
        val temporary = "PactoQA" + System.currentTimeMillis().toString().takeLast(7)
        val client = ApiClientFactory.defaultClient().newBuilder().retryOnConnectionFailure(false).build()
        val api = ApiClientFactory().create(BuildConfig.API_BASE_URL, client).create(PactoApi::class.java)
        val memory = object : SessionStore {
            var value: Session? = null
            override fun read() = value
            override fun write(session: Session) { value = session }
            override fun clear() { value = null }
        }
        val partnerSessions = SessionManager(api, memory)
        try {
            compose.runOnIdle { model.editNickname(temporary) }
            compose.waitUntil(30000) { model.state.value.social.nicknameNotice != null }
            assertTrue(model.state.value.social.nicknameNotice.orEmpty().contains("disponible"))
            report("USR-06 availability PASS")
            compose.runOnIdle { model.saveProfile() }
            awaitOperation()
            assertEquals(temporary, model.state.value.account?.nickname)
            compose.runOnIdle { model.loadProfile() }
            awaitOperation()
            assertEquals(temporary, model.state.value.social.profile?.account?.nickname)
            report("USR-03 profile patch and GET PASS")

            compose.runOnIdle { model.navigate(ConnectedRoute.GROUPS) }
            awaitOperation()
            val group = model.state.value.social.groups.items.first { it.name == "QA pruebas Android" }
            report("GRP-02 groups PASS count=${model.state.value.social.groups.items.size}")
            compose.runOnIdle { model.selectGroup(group.id) }
            awaitOperation()
            assertEquals(group.id, model.state.value.group?.id)
            report("GRP-03 group detail PASS")

            partnerSessions.login(LoginRequest(requireNotNull(args.getString("partner.email")),
                requireNotNull(args.getString("partner.password"))))
            val partnerRepo = ApiSocialRepository(api, partnerSessions)
            val joined = runCatching { partnerRepo.join(group.inviteCode.lowercase()) }.getOrElse {
                if (it is ApiFailure && it.code == "ALREADY_MEMBER") partnerRepo.group(group.id) else throw it
            }
            check(joined.id == group.id)
            report("MEM-01 join PASS (second QA account, existing group)")
            compose.runOnIdle { model.navigate(ConnectedRoute.MEMBERS) }
            awaitOperation()
            assertTrue(model.state.value.social.members.items.any { it.id != owner.id })
            compose.onAllNodesWithText(temporary)[0].assertExists()
            report("MEM-02 members PASS")

            val partnerProducts = ApiProductRepository(api, partnerSessions)
            val bond = joined.bonds.min()
            val deadline = "2099-11-10T23:59:00Z"
            val first = partnerProducts.createChallenge(joined, ChallengeDraft("PactoGG QA",
                "Complete a friendly game", "Choose the next game", deadline, bond, owner.id))
            compose.runOnIdle { model.navigate(ConnectedRoute.BOARD) }
            awaitOperation()
            assertTrue(model.state.value.social.contracts.items.any { it.id == first.id })
            report("CON-02 board PASS")
            compose.runOnIdle { model.openChallenge(first) }
            awaitOperation()
            assertEquals(owner.id, model.state.value.contract?.targetId)
            assertNotNull(model.state.value.contract?.version)
            report("CON-03 detail PASS")
            compose.runOnIdle { model.requestContractAction("accept") }
            awaitOperation()
            assertEquals("accept", model.state.value.social.pendingAction)
            compose.onNodeWithText("Confirmar aceptación").assertExists()
            compose.runOnIdle { model.confirmContractAction() }
            awaitOperation()
            assertEquals("in_progress", model.state.value.contract?.status)
            assertEquals(group.points - bond, model.state.value.group?.points)
            report("CON-06 accept and real points PASS")

            val second = partnerProducts.createChallenge(joined, ChallengeDraft("PactoGG QA",
                "Play a friendly match", "Choose the next game", deadline, bond, owner.id))
            compose.runOnIdle { model.openChallenge(second) }
            awaitOperation()
            val beforeReject = model.state.value.group?.points
            compose.runOnIdle { model.requestContractAction("reject") }
            awaitOperation()
            compose.onNodeWithText("Confirmar rechazo").assertExists()
            compose.runOnIdle { model.confirmContractAction() }
            awaitOperation()
            assertEquals("cancelled", model.state.value.contract?.status)
            assertEquals("rejected", model.state.value.contract?.closedReason)
            assertEquals(beforeReject, model.state.value.group?.points)
            report("CON-07 reject without points change PASS")
        } finally {
            if (model.state.value.account?.id == owner.id && model.state.value.account?.nickname != original) {
                compose.runOnIdle { model.editNickname(original); model.saveProfile() }
                awaitOperation()
                assertEquals(original, model.state.value.account?.nickname)
                report("QA nickname restored PASS")
            }
            if (partnerSessions.current.value != null) partnerSessions.logout()
        }
    }
    private fun awaitOperation() {
        compose.waitUntil(60000) { !model.state.value.busy }
        compose.waitForIdle()
        check(model.state.value.error == null) {
            "QA operation failed: ${model.state.value.diagnostic}; ${model.state.value.error}"
        }
    }
    private fun report(step: String) {
        InstrumentationRegistry.getInstrumentation().sendStatus(0, Bundle().apply { putString("stream", "$step\n") })
    }
}
