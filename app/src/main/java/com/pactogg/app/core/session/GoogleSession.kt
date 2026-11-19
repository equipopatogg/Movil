package com.pactogg.app.core.session

import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.core.network.failure
import com.pactogg.app.feature.auth.data.GoogleRequest

private val GoogleNickname = Regex("[A-Za-z0-9_.-]{3,24}")
private const val MinToken = 20
private const val MaxToken = 4096

/**
 * Acceso con Google (AUTH-02). Google solo entrega el ID token; la sesión de PactoGG la crea el backend
 * y se guarda igual que la de correo. 200 = cuenta existente, 201 = cuenta nueva.
 */
suspend fun SessionManager.google(body: GoogleRequest) {
    require(body.idToken.length in MinToken..MaxToken && body.idToken.none(Char::isWhitespace))
    require(body.nickname == null || (body.nickname.matches(GoogleNickname) && body.acceptPrivacy == true))
    authenticate {
        val response = api.google(body)
        if (response.code() != 200 && response.code() != 201) throw response.failure()
        response.body()?.data ?: throw ApiFailure(0, "INVALID_RESPONSE")
    }
}
