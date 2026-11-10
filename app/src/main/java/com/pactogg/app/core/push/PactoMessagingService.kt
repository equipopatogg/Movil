package com.pactogg.app.core.push

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull

@AndroidEntryPoint
class PactoMessagingService : FirebaseMessagingService() {
    @Inject lateinit var push: PushRuntime
    override fun onNewToken(token: String) {
        runBlocking { withTimeoutOrNull(8000) { push.newToken(token) } }
    }
    override fun onMessageReceived(message: RemoteMessage) {
        runBlocking { withTimeoutOrNull(8000) { push.receive(message.data["type"]) } }
    }
}
