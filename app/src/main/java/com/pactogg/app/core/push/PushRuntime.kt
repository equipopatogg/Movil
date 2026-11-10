package com.pactogg.app.core.push

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.pactogg.app.BuildConfig
import com.pactogg.app.R
import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.core.network.PactoApi
import com.pactogg.app.core.network.checked
import com.pactogg.app.core.session.SessionManager
import com.pactogg.app.feature.account.data.DeviceRequest
import com.pactogg.app.feature.account.data.expectStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout

@Singleton
class PushRuntime @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: PactoApi,
    private val sessions: SessionManager,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val mutableConnection = MutableStateFlow(PushConnectionState.NOT_CONFIGURED)
    val connection = mutableConnection.asStateFlow()
    private var messaging: FirebaseMessaging? = null
    @Volatile private var latestToken: String? = null
    @Volatile private var loggingOut = false
    private val registration = PushRegistration({ sessions.current.value?.account?.id },
        register = { owner, token ->
            val device = sessions.authorized { bearer ->
                checkOwner(owner)
                api.registerDevice(bearer, DeviceRequest(token, version = BuildConfig.VERSION_NAME))
            }.checked(200).data
            if (device.platform != "android" || device.id.isBlank()) throw ApiFailure(0, "INVALID_RESPONSE")
        }, remove = { owner, token ->
            sessions.authorized { bearer ->
                checkOwner(owner)
                api.removeDevice(bearer, token)
            }.expectStatus(204)
        }, stateChanged = { mutableConnection.value = it })

    fun start() {
        createPushChannel(context)
        val firebase = FirebaseApp.initializeApp(context)
        if (firebase != null && firebase.options.projectId == context.getString(R.string.project_id)) {
            messaging = FirebaseMessaging.getInstance()
            mutableConnection.value = PushConnectionState.SIGN_IN_REQUIRED
        }
        sessions.beforeLogout = { loggingOut = true; detach() }
        scope.launch {
            safely { sessions.restore() }
            sessions.current.map { it?.account?.id }.distinctUntilChanged().collect { owner ->
                NotificationManagerCompat.from(context).cancel(301)
                if (owner != null) {
                    loggingOut = false
                    registration.allowSession()
                    synchronize()
                } else {
                    if (messaging != null) mutableConnection.value = PushConnectionState.SIGN_IN_REQUIRED
                    messaging?.isAutoInitEnabled = false
                    safely { withTimeout(3000) { messaging?.deleteToken()?.awaitPush() } }
                    latestToken = null
                }
            }
        }
    }

    fun refresh() { scope.launch { synchronize() } }

    suspend fun newToken(token: String) {
        latestToken = token.takeIf(::validPushToken)
        safely {
            sessions.restore()
            registration.sync(token, pushAllowed(context) && !loggingOut)
        }
    }

    suspend fun receive(type: String?) {
        if (!knownPushType(type)) return
        safely {
            sessions.restore()
            if (sessions.current.value != null && !loggingOut) showPush(context)
        }
    }

    private suspend fun synchronize() = safely {
        val client = messaging ?: return@safely
        if (sessions.current.value == null) return@safely
        if (loggingOut) return@safely
        if (!pushAllowed(context)) {
            mutableConnection.value = PushConnectionState.PERMISSION_REQUIRED
            detach()
            return@safely
        }
        client.isAutoInitEnabled = true
        registration.allowSession()
        val token = obtainToken(client)
        latestToken = token
        if (!pushAllowed(context)) mutableConnection.value = PushConnectionState.PERMISSION_REQUIRED
        registration.sync(token, pushAllowed(context) && !loggingOut)
    }

    private suspend fun obtainToken(client: FirebaseMessaging): String {
        val owner = sessions.current.value?.account?.id
        mutableConnection.value = PushConnectionState.CONNECTING
        var confirmed = false
        try {
            return withTimeout(10000) { client.token.awaitPush() }.also { confirmed = true }
        } finally {
            if (!confirmed && sessions.current.value?.account?.id == owner) {
                mutableConnection.value = PushConnectionState.TOKEN_ERROR
            }
        }
    }

    private suspend fun detach(): Boolean {
        val client = messaging ?: return true
        client.isAutoInitEnabled = false
        var removed = false
        var deleted = false
        // Independent bounded attempts: local logout must work offline too.
        safely { removed = withTimeout(5000) { registration.detach(latestToken) } }
        safely { withTimeout(3000) { client.deleteToken().awaitPush() }; deleted = true }
        latestToken = null
        NotificationManagerCompat.from(context).cancel(301)
        return removed && deleted
    }

    private fun checkOwner(owner: String) {
        if (sessions.current.value?.account?.id != owner) throw ApiFailure(0, "SESSION_CHANGED")
    }
}

private suspend fun safely(block: suspend () -> Unit) {
    try { block() } catch (_: kotlinx.coroutines.TimeoutCancellationException) {
        // Bounded provider operations may time out; preserve the next explicit retry event.
    } catch (error: CancellationException) {
        throw error
    } catch (_: Exception) {
        // No token/provider payload logging. Retry only on next foreground/session/token event.
    }
}

private suspend fun <T> Task<T>.awaitPush(): T = suspendCancellableCoroutine { continuation ->
    addOnCompleteListener { task ->
        if (continuation.isActive) {
            if (task.isSuccessful) continuation.resume(task.result)
            else continuation.resumeWithException(task.exception ?: IllegalStateException("FCM unavailable"))
        }
    }
}
