package com.pactogg.app.navigation

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.pactogg.app.MainActivity
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SafeAreaTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test fun contentAvoidsSystemBarsAndCutoutInPortraitAndLandscape() {
        fun checkBounds() {
            compose.waitForIdle()
            val bounds = compose.onNodeWithTag("safe-app-content").fetchSemanticsNode().boundsInRoot
            compose.runOnIdle {
                val decor = compose.activity.window.decorView
                val insets = requireNotNull(ViewCompat.getRootWindowInsets(decor)).getInsets(
                    WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
                assertTrue("top avoids status/camera", bounds.top >= insets.top - 1)
                assertTrue("left avoids cutout", bounds.left >= insets.left - 1)
                assertTrue("right avoids cutout", bounds.right <= decor.width - insets.right + 1)
                assertTrue("bottom avoids navigation", bounds.bottom <= decor.height - insets.bottom + 1)
            }
        }
        checkBounds()
        try {
            compose.runOnIdle { compose.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE }
            compose.waitUntil(10000) { compose.activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE }
            checkBounds()
        } finally {
            compose.runOnIdle { compose.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT }
        }
    }
}
