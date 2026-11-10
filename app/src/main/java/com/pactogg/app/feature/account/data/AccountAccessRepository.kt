package com.pactogg.app.feature.account.data

import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.core.network.PactoApi
import com.pactogg.app.core.network.checked
import com.pactogg.app.core.network.failure
import com.pactogg.app.core.session.SessionManager
import javax.inject.Inject

class AccountAccessRepository @Inject constructor(private val api: PactoApi, private val sessions: SessionManager) {
    suspend fun verify(token: String) {
        validToken(token)
        if (!api.verifyEmail(TokenRequest(token)).checked(200).data.verified) invalidAccountResponse()
    }
    suspend fun resend() = sessions.authorized { api.resendEmail(it) }.expectStatus(202)
    suspend fun forgot(email: String) {
        require(email.length <= 254 && email.matches(Regex("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")))
        api.forgotPassword(EmailRequest(email)).expectStatus(202)
    }
    suspend fun reset(token: String, password: String) {
        validToken(token)
        validPassword(password)
        val observed = sessions.current.value
        if (!api.resetPassword(ResetRequest(token, password)).checked(200).data.reset) invalidAccountResponse()
        sessions.accounts.clear(observed)
    }
    suspend fun password(current: String, password: String) {
        require(current.length in 1..128)
        validPassword(password)
        val account = sessions.current.value?.account?.id ?: throw ApiFailure(401, "UNAUTHORIZED")
        sessions.accounts.commit {
            sessions.authorized { api.changePassword(it, PasswordRequest(current, password)) }.checked(200).data.also {
                if (it.user.id != account) invalidAccountResponse()
            }
        }
    }
    suspend fun google(token: String, nickname: String?, consent: Boolean, version: String) {
        require(token.length in 20..4096 && token.none(Char::isWhitespace))
        if (nickname != null) {
            require(nickname.matches(Regex("[A-Za-z0-9_.-]{3,24}")) && consent)
            require(version.matches(Regex("[A-Za-z0-9_.-]{1,32}")))
        }
        sessions.accounts.commit {
            val response = api.google(GoogleRequest(token, nickname, consent.takeIf { nickname != null },
                version.takeIf { nickname != null }))
            if (response.code() !in listOf(200, 201)) throw response.failure()
            response.body()?.data ?: throw ApiFailure(0, "INVALID_RESPONSE")
        }
    }
    suspend fun consent(version: String) {
        require(version.matches(Regex("[A-Za-z0-9_.-]{1,32}")))
        val result = sessions.authorized { api.consent(it, ConsentRequest(version)) }.checked(200).data
        if (result.version != version || result.acceptedAt.isBlank()) invalidAccountResponse()
    }
}

internal fun validToken(value: String) { require(value.length in 16..128 && value.none(Char::isWhitespace)) }
internal fun validPassword(value: String) { require(value.length in 12..128) }
internal fun validObjectId(value: String) { require(value.matches(Regex("[a-fA-F0-9]{24}"))) }
internal fun invalidAccountResponse(): Nothing = throw ApiFailure(0, "INVALID_RESPONSE")
internal fun retrofit2.Response<*>.expectStatus(expected: Int) {
    if (code() != expected) throw failure()
}
