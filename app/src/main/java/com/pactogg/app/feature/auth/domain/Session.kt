package com.pactogg.app.feature.auth.domain

data class Account(val id: String, val email: String, val nickname: String)

/** Deliberately not a data class: generated toString/copy must not expose tokens. */
class Session(
    val account: Account,
    val accessToken: String,
    val refreshToken: String,
    val expiresAtMillis: Long,
)

interface SessionStore {
    fun read(): Session?
    fun write(session: Session)
    fun clear()
}
