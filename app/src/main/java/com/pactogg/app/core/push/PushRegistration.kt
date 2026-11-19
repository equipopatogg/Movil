package com.pactogg.app.core.push

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

enum class PushConnectionState {
    NOT_CONFIGURED, SIGN_IN_REQUIRED, PERMISSION_REQUIRED, CONNECTING,
    REGISTERING, REGISTERED, TOKEN_ERROR, SERVER_ERROR,
}

/** Serializa registro, rotación y baja (PL-10 RF2, RF3). El token solo vive en memoria. */
internal class PushRegistration(
    private val account: () -> String?,
    private val register: suspend (String, String) -> Unit,
    private val remove: suspend (String, String) -> Unit,
    private val stateChanged: (PushConnectionState) -> Unit = {},
) {
    private val mutex = Mutex()
    private var registered: Pair<String, String>? = null
    private var closing = false

    suspend fun sync(token: String, permitted: Boolean) = mutex.withLock {
        val allowed = permitted && !closing && validPushToken(token)
        val owner = account()?.takeIf { allowed } ?: return@withLock
        val next = owner to token
        if (registered == next) {
            stateChanged(PushConnectionState.REGISTERED)
            return@withLock
        }
        stateChanged(PushConnectionState.REGISTERING)
        var confirmed = false
        try {
            registered?.takeIf { it.first == owner && it.second != token }?.let { remove(owner, it.second) }
            register(owner, token)
            if (account() == owner) {
                registered = next
                confirmed = true
                stateChanged(PushConnectionState.REGISTERED)
            }
        } finally {
            if (!confirmed && account() == owner) stateChanged(PushConnectionState.SERVER_ERROR)
        }
    }

    /** Devuelve true si no quedó nada registrado a nombre de la cuenta actual. */
    suspend fun detach(token: String?): Boolean = mutex.withLock {
        closing = true
        val owner = account()
        val target = token?.takeIf(::validPushToken) ?: registered?.takeIf { it.first == owner }?.second
        try {
            if (owner != null && target != null) remove(owner, target)
            owner == null || target != null
        } finally {
            registered = null
        }
    }

    suspend fun allowSession() = mutex.withLock { closing = false }
}

internal fun validPushToken(value: String): Boolean = value.length in 20..4096 && value.none(Char::isWhitespace)
