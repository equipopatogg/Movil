package com.pactogg.app.design

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.key
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.platform.app.InstrumentationRegistry
import com.pactogg.app.prototype.*
import com.pactogg.app.feature.auth.presentation.AccessScreen
import com.pactogg.app.feature.auth.presentation.AccessState
import com.pactogg.app.feature.home.presentation.ContentStatus
import com.pactogg.app.ui.theme.PactoGGTheme
import java.io.File
import org.junit.Rule
import org.junit.Test

/** Renders real native components for manual visual review, not pixel acceptance. */
class DesignSnapshots {
    @get:Rule val compose = createComposeRule()

    @Test fun renderDesignReferences() {
        val selected = mutableStateOf("access")
        compose.setContent {
            PactoGGTheme {
                key(selected.value) {
                    when (selected.value) {
                        "access" -> AccessScreen(AccessState(), {})
                        "register" -> AccessScreen(AccessState(registering = true), {})
                        "home" -> HomePage({})
                        "explore" -> ExploreRoute(ContentStatus.EMPTY, {})
                        "groups" -> GroupsPage()
                        "profile" -> ProfilePage({})
                        "create" -> ManualChallengeScreen(null, {})
                        "ranking" -> RankingPage()
                        "voice" -> VoicePage({})
                        "notifications" -> NotificationsPage({})
                        "recovery" -> RecoveryPage({})
                        "welcome" -> WelcomePage({})
                        else -> if (selected.value in listOf("contract", "evidence", "jury")) {
                            ReviewPage(selected.value, {})
                        } else TournamentPage(selected.value, {})
                    }
                }
            }
        }
        val folder = File(InstrumentationRegistry.getInstrumentation().targetContext.cacheDir, "design-review")
        folder.mkdirs()
        val routes = listOf("access", "register", "home", "explore", "groups", "profile", "create", "ranking",
            "voice", "notifications", "recovery", "welcome", "contract", "evidence", "jury",
            "tournaments", "new-tournament", "bracket", "match")
        routes.forEach { route ->
            compose.runOnIdle { selected.value = route }
            compose.waitForIdle()
            val bitmap = compose.onRoot().captureToImage().asAndroidBitmap()
            File(folder, "$route.png").outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        }
    }
}
