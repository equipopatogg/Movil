package com.pactogg.app.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.pactogg.app.MainActivity
import org.junit.Rule
import org.junit.Test

class ShellNavigationTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun switchesTabsAndReturnsFromGroups() {
        compose.onNodeWithTag("nav_tournaments").performClick()
        compose.onNodeWithText("El espacio para competir con tu grupo. Las inscripciones serán por equipos.")
            .assertIsDisplayed()
        compose.onNodeWithTag("nav_profile").performClick()
        compose.onNodeWithText("Mis grupos").performClick()
        compose.onNodeWithText("Volver al perfil").performClick()
        compose.onNodeWithTag("nav_profile").assertIsDisplayed()
        compose.onNodeWithTag("nav_contracts").performClick()
        compose.onNodeWithText("Tus retos y pactos aparecerán aquí cuando conectemos tu cuenta.")
            .assertIsDisplayed()
    }
}
