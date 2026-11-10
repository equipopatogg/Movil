package com.pactogg.app

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.combine
import com.pactogg.app.feature.account.presentation.ExpandedApp
import com.pactogg.app.feature.account.presentation.ExtrasViewModel
import androidx.activity.SystemBarStyle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.pactogg.app.feature.connected.presentation.ConnectedViewModel
import com.pactogg.app.ui.theme.PactoGGTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @javax.inject.Inject lateinit var push: com.pactogg.app.core.push.PushRuntime
    private var pushPending = false
    private lateinit var connected: ConnectedViewModel
    private lateinit var extras: ExtrasViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
        )
        val model = ViewModelProvider(this)[ConnectedViewModel::class.java]
        connected = model
        extras = ViewModelProvider(this)[ExtrasViewModel::class.java]
        pushPending = savedInstanceState?.getBoolean("push_pending") == true
        // The saved flag already records whether this launch notification was consumed.
        if (savedInstanceState == null) consumePush(intent)
        consumeAccountLink(intent)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                combine(connected.state, extras.state) { _, _ -> Unit }.collect {
                    openPendingPush()
                }
            }
        }
        setContent { PactoGGTheme { ExpandedApp(model, extras) } }
    }
    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        consumePush(intent)
        consumeAccountLink(intent)
    }

    override fun onResume() {
        super.onResume()
        push.refresh()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("push_pending", pushPending)
        super.onSaveInstanceState(outState)
    }

    private fun consumePush(value: android.content.Intent) {
        if (value.action == com.pactogg.app.core.push.PushOpen ||
            com.pactogg.app.core.push.knownPushType(value.getStringExtra("type"))) pushPending = true
        // FCM notification+data messages arrive here when opened from background.
        value.removeExtra("type")
        // Keep the launch action: Android and ActivityScenario use it to identify this activity.
        openPendingPush()
    }

    private fun openPendingPush() {
        val state = connected.state.value
        val ready = !state.restoring && state.account != null && !state.busy
        if (extras.state.value.busy) return
        if (pushPending && ready) {
            pushPending = false
            extras.open(null)
            connected.navigate(com.pactogg.app.feature.connected.presentation.ConnectedRoute.NOTIFICATIONS)
        }
    }

    private fun consumeAccountLink(value: android.content.Intent) {
        val raw = value.dataString
        setIntent(android.content.Intent(value).setData(null))
        lifecycleScope.launch {
            connected.state.first { !it.restoring }
            extras.link(raw)
        }
    }
}
