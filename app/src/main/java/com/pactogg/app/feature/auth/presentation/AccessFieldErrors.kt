package com.pactogg.app.feature.auth.presentation

import com.pactogg.app.feature.connected.domain.InputRules

internal fun accessEmailError(state: AccessState, code: String?, message: String?): String? = when {
    code == "EMAIL_ALREADY_EXISTS" -> message
    state.submitted && !InputRules.access(state.email, "valid", null, false) ->
        "Escribe un correo válido, como nombre@correo.com."
    else -> null
}

internal fun accessPasswordError(state: AccessState, code: String?, message: String?): String? = when {
    code == "INVALID_CREDENTIALS" -> message
    state.submitted && state.password.length !in (if (state.registering) 12 else 1)..128 ->
        if (state.registering) "Usa entre 12 y 128 caracteres." else "Escribe tu contraseña."
    else -> null
}

internal fun accessNicknameError(state: AccessState, code: String?, message: String?): String? = when {
    code == "NICKNAME_ALREADY_EXISTS" -> message
    state.submitted && !state.nickname.trim().matches(Regex("[A-Za-z0-9_.-]{3,24}")) ->
        "Usa de 3 a 24 letras, números, puntos o guiones."
    else -> null
}
