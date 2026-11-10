package com.pactogg.app.feature.account.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pactogg.app.feature.connected.presentation.ConnectedApp
import com.pactogg.app.feature.connected.presentation.ConnectedRoute
import com.pactogg.app.feature.connected.presentation.ConnectedViewModel
import com.pactogg.app.feature.connected.presentation.SafeAppContent
import com.pactogg.app.feature.connected.presentation.loadCommunity
import com.pactogg.app.feature.connected.presentation.loadProfile
import com.pactogg.app.feature.connected.presentation.returnFromMaintenance

val LocalExtras = staticCompositionLocalOf<ExtrasViewModel?> { null }

@Composable
fun ExpandedApp(model: ConnectedViewModel, extras: ExtrasViewModel) {
    val state by extras.state.collectAsStateWithLifecycle()
    var previous by remember { mutableStateOf<ExtrasPage?>(null) }
    LaunchedEffect(state.page) {
        if (state.page == null) {
            when (previous) {
                ExtrasPage.EDIT, ExtrasPage.CANCEL -> model.returnFromMaintenance()
                ExtrasPage.DELETE_NOTICE -> model.loadCommunity()
                ExtrasPage.VERIFY, ExtrasPage.CONSENT, ExtrasPage.PASSWORD ->
                    if (model.state.value.account != null) model.loadProfile()
                else -> Unit
            }
        }
        previous = state.page
    }
    if (state.busy && state.page == null) androidx.compose.ui.window.Dialog(
        onDismissRequest = {}, properties = androidx.compose.ui.window.DialogProperties(
            dismissOnBackPress = false, dismissOnClickOutside = false)) {
        androidx.compose.material3.Surface {
            androidx.compose.material3.CircularProgressIndicator()
        }
    }
    CompositionLocalProvider(LocalExtras provides extras) {
        if (state.page == null) ConnectedApp(model)
        else SafeAppContent {
            BackHandler(!state.busy) { extras.open(null) }
            ExtrasScreen(state, extras)
        }
    }
}
