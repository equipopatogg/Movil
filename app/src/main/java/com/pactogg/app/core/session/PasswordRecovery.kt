package com.pactogg.app.core.session

import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.core.network.failure
import com.pactogg.app.feature.auth.data.ForgotPasswordRequest
import com.pactogg.app.feature.auth.data.ResetPasswordRequest

internal val RecoveryEmail = Regex("[^@\\s]{1,64}@[^@\\s]+\\.[^@\\s]+")
internal val RecoveryToken = Regex("[A-Za-z0-9_-]{16,128}")

internal fun validRecoveryPassword(value: String) = value.length in 12..128

/** AUTH-06: pide el enlace. El servidor responde 202 exista o no la cuenta; no crea sesión. */
suspend fun SessionManager.forgotPassword(email: String) {
    val clean = email.trim()
    require(clean.length <= 254 && clean.matches(RecoveryEmail))
    val response = api.forgotPassword(ForgotPasswordRequest(clean))
    if (response.code() != 202) throw response.failure()
}

/** AUTH-07: cambia la contraseña con el token del enlace. El servidor cierra todas las sesiones de la cuenta. */
suspend fun SessionManager.resetPassword(token: String, password: String) {
    require(token.matches(RecoveryToken) && validRecoveryPassword(password))
    val response = api.resetPassword(ResetPasswordRequest(token, password))
    if (response.code() != 200) throw response.failure()
    if (response.body()?.data?.passwordReset != true) throw ApiFailure(0, "INVALID_RESPONSE")
}
