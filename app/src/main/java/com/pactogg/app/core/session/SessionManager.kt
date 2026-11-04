package com.pactogg.app.core.session

import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.core.network.PactoApi
import com.pactogg.app.core.network.checked
import com.pactogg.app.core.network.failure
import com.pactogg.app.feature.auth.data.LoginRequest
import com.pactogg.app.feature.auth.data.LogoutRequest
import com.pactogg.app.feature.auth.data.RefreshRequest
import com.pactogg.app.feature.auth.data.RegisterRequest
import com.pactogg.app.feature.auth.data.SessionDto
import com.pactogg.app.feature.auth.domain.Session
import com.pactogg.app.feature.auth.domain.SessionStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import retrofit2.Response

class SessionManager(private val api: PactoApi, private val store: SessionStore,
    private val now: () -> Long = System::currentTimeMillis) {
    private val mutex = Mutex()
    private val mutable = MutableStateFlow<Session?>(null)
    val current = mutable.asStateFlow()
    private var initialized = false
    private var generation = 0L

    suspend fun restore() = withContext(Dispatchers.IO) {
        mutex.withLock {
            if (!initialized) {
                mutable.value = store.read()
                initialized = true
            }
        }
        if (mutable.value != null && requireNotNull(mutable.value).expiresAtMillis <= now() + 30000) usable()
    }

    suspend fun login(body: LoginRequest) = authenticate { api.login(body).checked(200).data }

    suspend fun register(body: RegisterRequest) = authenticate { api.register(body).checked(201).data }

    private suspend fun authenticate(call: suspend () -> SessionDto) {
        val ticket = mutex.withLock { generation }
        val session = call().toDomain(now())
        withContext(Dispatchers.IO) {
            mutex.withLock {
                if (generation != ticket) throw ApiFailure(0, "SESSION_CHANGED")
                store.write(session)
                generation++
                mutable.value = session
                initialized = true
            }
        }
    }

    /** Serializes refresh, but does not hold the lock across business requests. */
    private suspend fun usable(previous: Session? = null): Session = withContext(Dispatchers.IO) {
        mutex.withLock {
            val session = mutable.value ?: throw ApiFailure(401, "UNAUTHORIZED")
            if (previous != null && previous !== session) return@withLock session
            if (previous == null && session.expiresAtMillis > now() + 30000) return@withLock session
            var committed = false
            try {
                val fresh = api.refresh(RefreshRequest(session.refreshToken)).checked(200).data.toDomain(now())
                store.write(fresh)
                mutable.value = fresh
                committed = true
                fresh
            } finally {
                // A lost rotating-refresh response cannot safely be replayed with the old token.
                if (!committed) withContext(NonCancellable) { clearLocked() }
            }
        }
    }

    suspend fun <T> authorized(readOnly: Boolean = false,
        call: suspend (String) -> Response<T>): Response<T> {
        var session = usable()
        val ticket = mutex.withLock {
            if (mutable.value !== session) throw ApiFailure(0, "SESSION_CHANGED")
            generation
        }
        var response = call("Bearer ${session.accessToken}")
        ensureGeneration(ticket)
        if (response.code() == 401) {
            response.errorBody()?.close()
            session = usable(session)
            ensureGeneration(ticket)
            if (!readOnly) throw ApiFailure(401, "SESSION_RENEWED")
            response = call("Bearer ${session.accessToken}")
            ensureGeneration(ticket)
            if (response.code() == 401) {
                withContext(Dispatchers.IO + NonCancellable) { mutex.withLock { clearLocked() } }
            }
        }
        return response
    }

    private suspend fun ensureGeneration(ticket: Long) = mutex.withLock {
        if (ticket != generation || mutable.value == null) throw ApiFailure(0, "SESSION_CHANGED")
    }

    /** Return true only when remote revocation is confirmed. Always close locally. */
    suspend fun logout(): Boolean = withContext(Dispatchers.IO) {
        val previous = mutex.withLock {
            val value = mutable.value
            withContext(NonCancellable) { clearLocked() }
            value
        } ?: return@withContext true
        val response = api.logout("Bearer ${previous.accessToken}", LogoutRequest(previous.refreshToken))
        if (response.code() != 204) throw response.failure()
        true
    }

    private fun clearLocked() {
        generation++
        mutable.value = null
        store.clear()
    }
}
