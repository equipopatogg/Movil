package com.pactogg.app.integration

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.isToggleable
import androidx.test.platform.app.InstrumentationRegistry
import androidx.lifecycle.ViewModelProvider
import com.pactogg.app.BuildConfig
import com.pactogg.app.MainActivity
import com.pactogg.app.core.network.ApiClientFactory
import com.pactogg.app.core.network.PactoApi
import com.pactogg.app.feature.auth.data.EncryptedSessionStore
import com.pactogg.app.feature.auth.data.RefreshRequest
import com.pactogg.app.feature.auth.domain.Session
import com.pactogg.app.feature.connected.presentation.ConnectedViewModel
import com.pactogg.app.feature.connected.presentation.ConnectedRoute
import java.io.ByteArrayOutputStream
import java.io.File
import java.security.MessageDigest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assume.assumeTrue
import org.junit.Rule
import org.junit.Test

/** Opt-in only: creates real QA data. Never runs against production by default. */
class LiveBackendTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()
    private lateinit var model: ConnectedViewModel

    @Test fun verifyQaLoginLogoutAndInvalidCredentials() {
        val args = InstrumentationRegistry.getArguments()
        assumeTrue(args.getString("pactogg.live") == "true")
        val email = requireNotNull(args.getString("qa.email"))
        val password = requireNotNull(args.getString("qa.password"))
        compose.runOnIdle {
            model = ViewModelProvider(compose.activity)[ConnectedViewModel::class.java]
            compose.activity.window.decorView.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
        }
        compose.waitUntil(60000) { !model.state.value.restoring }
        check(model.state.value.account?.email == email)
        compose.runOnIdle { model.navigate(ConnectedRoute.PROFILE) }
        click("Cerrar sesión")
        awaitOperation()
        assertNull(model.state.value.account)
        assertNull(model.state.value.notice)
        report("QA_STEP final_ui_logout PASS")
        fill("Correo electrónico", email)
        fill("Contraseña", "wrong-test-password")
        click("Iniciar sesión")
        awaitOperation()
        assertEquals(401, model.state.value.diagnostic?.status)
        assertNull(model.state.value.account)
        report("QA_STEP invalid_credentials PASS")
        compose.onNodeWithText("Contraseña").performScrollTo().performTextReplacement(password)
        click("Iniciar sesión")
        awaitOperation()
        assertNull(model.state.value.error)
        assertEquals(email, model.state.value.account?.email)
        report("QA_STEP final_ui_login PASS")
    }

    @Test fun prepareExpiredQaSessionForColdStart() {
        val args = InstrumentationRegistry.getArguments()
        assumeTrue(args.getString("pactogg.live") == "true" && args.getString("qa.expiry") == "true")
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val store = EncryptedSessionStore(context)
        val current = requireNotNull(store.read())
        check(current.account.email == args.getString("qa.email"))
        File(context.cacheDir, "qa-refresh-before.sha256").writeText(digest(current.refreshToken))
        store.write(Session(current.account, current.accessToken, current.refreshToken, System.currentTimeMillis() - 1000))
        report("QA_STEP expiry_prepared PASS (test account only)")
    }

    @Test fun verifyColdStartRenewal() {
        val args = InstrumentationRegistry.getArguments()
        assumeTrue(args.getString("pactogg.live") == "true" && args.getString("qa.expiry") == "true")
        compose.runOnIdle { model = ViewModelProvider(compose.activity)[ConnectedViewModel::class.java] }
        compose.waitUntil(60000) { !model.state.value.restoring }
        check(model.state.value.account?.email == args.getString("qa.email"))
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val current = requireNotNull(EncryptedSessionStore(context).read())
        val before = File(context.cacheDir, "qa-refresh-before.sha256")
        check(digest(current.refreshToken) != before.readText())
        check(current.expiresAtMillis > System.currentTimeMillis())
        before.delete()
        report("QA_STEP cold_start_session_manager_refresh PASS")
    }

    private fun digest(value: String): String = MessageDigest.getInstance("SHA-256")
        .digest(value.toByteArray()).joinToString("") { "%02x".format(it) }

    @Test fun authorizedAccountAndAvailableFlows() {
        val args = InstrumentationRegistry.getArguments()
        assumeTrue(args.getString("pactogg.live") == "true")
        val email = requireNotNull(args.getString("qa.email"))
        val password = requireNotNull(args.getString("qa.password"))
        val nickname = requireNotNull(args.getString("qa.nickname"))
        compose.runOnIdle { model = ViewModelProvider(compose.activity)[ConnectedViewModel::class.java] }
        compose.waitUntil(10000) { !model.state.value.restoring }
        compose.runOnIdle {
            // Do not save synthetic test credentials in the device's password manager.
            compose.activity.window.decorView.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
        }
        if (model.state.value.account?.email == email) {
            compose.runOnIdle { model.logout() }
            awaitOperation()
        }
        check(model.state.value.account == null) { "Requires a signed-out app; does not replace another account." }

        val existing = args.getString("qa.existing") == "true"
        if (!existing) {
            click("¿Nuevo por aquí? Crea tu cuenta")
            fill("Apodo", nickname)
        }
        fill("Correo electrónico", email)
        fill("Contraseña", password)
        if (!existing) compose.onNode(isToggleable()).performScrollTo().performClick()
        click(if (existing) "Iniciar sesión" else "Crear cuenta")
        awaitOperation()
        assertNull(model.state.value.error)
        assertEquals(email, model.state.value.account?.email)
        report("QA_STEP ${if (existing) "existing_login" else "register"} PASS")

        click("Grupos")
        fill("Nombre del grupo", "QA pruebas Android")
        click("Crear grupo")
        awaitOperation()
        assertNull(model.state.value.error)
        assertNotNull(model.state.value.group)
        report("QA_STEP group PASS")

        click("Crear reto en este grupo")
        fill("Juego", "PactoGG QA")
        fill("Condición del reto", "Completar una partida de prueba sin dinero")
        fill("Recompensa sin dinero", "Elegir el juego de la siguiente partida")
        fill("Fecha local: AAAA-MM-DD HH:mm", "2026-11-10 18:00")
        click(requireNotNull(model.state.value.group).bonds.first().toString())
        click("Revisar reto")
        compose.waitForIdle()
        check(model.state.value.reviewing) {
            "Review rejected: ${model.state.value.error}; draft=${model.state.value.draft}"
        }
        click("Confirmar y crear reto")
        awaitOperation()
        val contractFailed = model.state.value.contract == null
        if (contractFailed) {
            report("QA_STEP contract FAIL ${model.state.value.diagnostic}")
            report("QA_STEP link/photo BLOCKED: no confirmed contract")
        } else {
            report("QA_STEP contract PASS")

            click("Agregar evidencia")
            fill("Enlace HTTPS", "https://youtu.be/pactogg-qa-test")
            click("Enviar enlace")
            awaitOperation()
            assertNull(model.state.value.error)
            assertEquals("link", model.state.value.evidence?.type)
            report("QA_STEP link PASS")

            val photo = harmlessImage()
            compose.runOnIdle { model.sendPhoto { photo to "image/png" } }
            awaitOperation()
            assertNull(model.state.value.error)
            assertEquals("photo", model.state.value.evidence?.type)
            report("QA_STEP photo_upload PASS (picker not automated)")

        }

        click("Torneos")
        awaitOperation()
        assertNull(model.state.value.error)
        check(model.state.value.tournamentsLoaded)
        report("QA_STEP tournaments PASS count=${model.state.value.tournaments.size}")

        verifyRotatingRefresh()
        compose.runOnIdle { model.navigate(ConnectedRoute.PROFILE) }
        click("Cerrar sesión")
        awaitOperation()
        assertNull(model.state.value.account)
        report("QA_STEP logout PASS")

        fill("Correo electrónico", email)
        fill("Contraseña", password)
        click("Iniciar sesión")
        awaitOperation()
        assertNull(model.state.value.error)
        assertEquals(email, model.state.value.account?.email)
        report("QA_STEP login PASS")
        check(!contractFailed) { "Contract creation failed on provider; see QA_STEP diagnostic. Other independent checks completed." }
    }

    private fun verifyRotatingRefresh() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val store = EncryptedSessionStore(context)
        val previous = requireNotNull(store.read())
        val api = ApiClientFactory().create(BuildConfig.API_BASE_URL).create(PactoApi::class.java)
        val response = api.refresh(RefreshRequest(previous.refreshToken))
        assertEquals(200, response.code())
        val fresh = requireNotNull(response.body()).data.toDomain(System.currentTimeMillis())
        check(fresh.refreshToken != previous.refreshToken)
        store.write(fresh)
        assertEquals(401, api.refresh(RefreshRequest(previous.refreshToken)).code())
        report("QA_STEP refresh_rotation PASS")
        // Logout uses the in-memory pre-rotation token; keep both test tokens accounted for.
        api.logout("Bearer ${fresh.accessToken}", com.pactogg.app.feature.auth.data.LogoutRequest(fresh.refreshToken))
    }

    private fun awaitOperation() {
        compose.waitUntil(60000) { !model.state.value.busy }
        compose.waitForIdle()
        model.state.value.error?.let { report("QA_ERROR $it ${model.state.value.diagnostic}") }
    }

    private fun click(text: String) {
        val node = compose.onAllNodes(hasText(text) and hasClickAction())[0]
        runCatching { node.performScrollTo() }
        node.performSemanticsAction(SemanticsActions.OnClick) { check(it()) }
        compose.waitForIdle()
    }

    private fun fill(label: String, value: String) {
        compose.onNodeWithText(label).performScrollTo().performTextInput(value)
    }

    private fun report(value: String) {
        InstrumentationRegistry.getInstrumentation().sendStatus(0, Bundle().apply {
            putString("stream", "$value\n")
        })
    }

    private fun harmlessImage(): ByteArray {
        val bitmap = Bitmap.createBitmap(640, 380, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.rgb(11, 16, 32))
        val paint = Paint().apply { color = Color.CYAN; textSize = 40f }
        canvas.drawText("PactoGG QA", 60f, 170f, paint)
        val result = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, result)
        bitmap.recycle()
        return result.toByteArray()
    }
}
