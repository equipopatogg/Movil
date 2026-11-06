package com.pactogg.app.feature.connected.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.pactogg.app.feature.auth.domain.Account
import com.pactogg.app.feature.connected.domain.Challenge
import com.pactogg.app.ui.theme.PactoGGTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class EvidenceUiTest {
    @get:Rule val compose = createComposeRule()
    private val contract = Challenge("contract", "group", "Halo", "Ganar", "Elegir juego",
        "2099-01-01T00:00:00Z", 25, "in_progress", targetType = "member", targetId = "qa", acceptedBy = "qa")

    @Test fun navigationKeepsFiveMakeEntriesAndExploreDoesNotNavigate() {
        val route = mutableStateOf(ConnectedRoute.HOME)
        var navigations = 0
        compose.setContent { PactoGGTheme {
            ConnectedNavigation(route.value, false) { route.value = it; navigations++ }
        } }
        listOf("Inicio", "Explorar", "Crear", "Torneos", "Grupos").forEach {
            compose.onNodeWithText(it).assertIsDisplayed()
        }
        compose.onNodeWithText("Explorar").performClick()
        compose.onNodeWithText("Esta sección estará disponible próximamente.").assertIsDisplayed()
        compose.onNodeWithText("Entendido").performClick()
        compose.onAllNodes(isDialog()).assertCountEquals(0)
        compose.runOnIdle { assertEquals(0, navigations) }
        compose.onNodeWithText("Grupos").performClick()
        compose.onNodeWithText("Grupos").assertIsSelected()
        compose.runOnIdle { assertEquals(ConnectedRoute.GROUPS, route.value); assertEquals(1, navigations) }
    }

    @Test fun unauthorizedReaderCanRefreshEvidenceWithoutUploadButtons() {
        var refreshes = 0
        compose.setContent { PactoGGTheme { Box(Modifier.fillMaxSize()) {
            EvidenceContent(ConnectedState(restoring = false, contract = contract,
                account = Account("other", "qa@example.com", "QA")), {}, {}, {}, {}, { refreshes++ }, {})
        } } }
        compose.onNodeWithText("Elegir foto").assertDoesNotExist()
        compose.onNodeWithText("Revisar enlace").assertDoesNotExist()
        compose.onNodeWithText("Actualizar evidencias").performScrollTo().performClick()
        compose.runOnIdle { assertEquals(1, refreshes) }
    }

    @Test fun photoPreviewCanBeDiscardedWithoutUploading() {
        val photo = com.pactogg.app.feature.connected.data.PreparedPhoto(byteArrayOf(1, 2), 20, 30)
        val state = mutableStateOf(ConnectedState(restoring = false, contract = contract, selectedPhoto = photo,
            account = Account("qa", "qa@example.com", "QA")))
        var uploads = 0
        compose.setContent { PactoGGTheme {
            EvidenceContent(state.value, {}, { uploads++ }, {}, {}, {}, {
                photo.clear(); state.value = state.value.copy(selectedPhoto = null)
            })
        } }
        compose.onNodeWithText("Enviar foto").assertExists()
        compose.onNodeWithText("Quitar foto").performScrollTo().performClick()
        compose.onNodeWithText("Elegir foto").assertExists()
        compose.runOnIdle { assertEquals(0, uploads); assertTrue(photo.bytes.all { it == 0.toByte() }) }
    }

    @Test fun linkConfirmationCanBeCancelledWithoutSending() {
        var sends = 0
        compose.setContent { PactoGGTheme {
            EvidenceContent(ConnectedState(restoring = false, contract = contract, link = "https://youtu.be/example",
                account = Account("qa", "qa@example.com", "QA")), {}, {}, { sends++ }, {}, {}, {})
        } }
        compose.onNodeWithText("Revisar enlace").performScrollTo().performClick()
        compose.onNodeWithText("Cancelar").performClick()
        compose.onAllNodes(isDialog()).assertCountEquals(0)
        compose.runOnIdle { assertEquals(0, sends) }
    }
}
