package com.pactogg.app

import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.pactogg.app.feature.connected.presentation.ConnectedApp
import com.pactogg.app.feature.connected.presentation.ConnectedViewModel
import com.pactogg.app.ui.theme.PactoGGTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
        )
        val model = ViewModelProvider(this)[ConnectedViewModel::class.java]
        setContent { PactoGGTheme { ConnectedApp(model) } }
    }
}
