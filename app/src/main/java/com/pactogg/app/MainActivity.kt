package com.pactogg.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.pactogg.app.core.push.PushEvents
import com.pactogg.app.feature.connected.presentation.ConnectedApp
import com.pactogg.app.feature.connected.presentation.ConnectedViewModel
import com.pactogg.app.feature.connected.presentation.resetLink
import com.pactogg.app.ui.theme.PactoGGTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var model: ConnectedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) PushEvents.handle(intent)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
        )
        model = ViewModelProvider(this)[ConnectedViewModel::class.java]
        if (savedInstanceState == null) model.resetLink(resetToken(intent))
        setContent { PactoGGTheme { ConnectedApp(model) } }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        PushEvents.handle(intent)
        model.resetLink(resetToken(intent))
    }
}

/** Solo `pactogg://auth/reset-password?token=…` (PL-11). Cualquier otro enlace se ignora. */
private fun resetToken(intent: Intent?): String? = intent?.data?.takeIf {
    it.scheme == "pactogg" && it.host == "auth" && it.path == "/reset-password"
}?.getQueryParameter("token")
