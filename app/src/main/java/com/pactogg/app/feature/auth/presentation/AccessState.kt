package com.pactogg.app.feature.auth.presentation

data class AccessState(
    val registering: Boolean = false,
    val nickname: String = "",
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val submitted: Boolean = false,
    val loading: Boolean = false,
    val acceptedPrivacy: Boolean = false,
)

sealed interface AccessEvent {
    data class Nickname(val value: String) : AccessEvent
    data class Email(val value: String) : AccessEvent
    data class Password(val value: String) : AccessEvent
    data class AcceptPrivacy(val accepted: Boolean) : AccessEvent
    data object TogglePassword : AccessEvent
    data object SwitchMode : AccessEvent
    data object Submit : AccessEvent
}

/** Presentation only. No authentication, storage or network effects. */
fun AccessState.reduce(event: AccessEvent): AccessState = when (event) {
    is AccessEvent.Nickname -> copy(nickname = event.value, submitted = false)
    is AccessEvent.Email -> copy(email = event.value, submitted = false)
    is AccessEvent.Password -> copy(password = event.value, submitted = false)
    is AccessEvent.AcceptPrivacy -> copy(acceptedPrivacy = event.accepted)
    AccessEvent.TogglePassword -> copy(passwordVisible = !passwordVisible)
    AccessEvent.SwitchMode -> AccessState(registering = !registering)
    AccessEvent.Submit -> copy(submitted = true)
}
