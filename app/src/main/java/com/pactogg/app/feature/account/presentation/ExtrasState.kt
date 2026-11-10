package com.pactogg.app.feature.account.presentation

import com.pactogg.app.feature.account.domain.NotificationChoices
import com.pactogg.app.feature.connected.domain.Challenge

enum class ExtrasPage {
    VERIFY, RESEND, FORGOT, RESET, PASSWORD, CONSENT, MEMBER, AUDIT, EDIT, CANCEL, DELETE_NOTICE,
    PREFERENCES, SERVICE, GOOGLE_PROFILE,
}

data class ExtrasState(
    val page: ExtrasPage? = null,
    val fields: Map<String, String> = emptyMap(),
    val resource: String = "",
    val contract: Challenge? = null,
    val busy: Boolean = false,
    val confirmation: Boolean = false,
    val accepted: Boolean = false,
    val lines: List<String> = emptyList(),
    val cursor: String? = null,
    val choices: NotificationChoices? = null,
    val error: String? = null,
) {
    override fun toString(): String = "ExtrasState(redacted)"
}

internal class ExtrasResult(
    val lines: List<String> = emptyList(),
    val cursor: String? = null,
    val choices: NotificationChoices? = null,
    val close: Boolean = false,
)
