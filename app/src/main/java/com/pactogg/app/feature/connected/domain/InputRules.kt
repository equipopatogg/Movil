package com.pactogg.app.feature.connected.domain

import java.net.URI
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object InputRules {
    fun access(email: String, password: String, nickname: String?, accepted: Boolean): Boolean =
        email.trim().length in 3..254 && email.trim().matches(Regex("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) &&
            password.length in (if (nickname == null) 1 else 12)..128 &&
            (nickname == null || (nickname.trim().matches(Regex("[A-Za-z0-9_.-]{3,24}")) && accepted))

    fun challenge(group: Group, draft: ChallengeDraft, now: Long): Boolean =
        draft.game.trim().length in 1..60 && draft.condition.trim().length in 1..280 &&
            draft.reward.trim().length in 3..80 && !MoneyPattern.containsMatchIn(draft.reward) &&
            draft.bond in group.bonds && (deadlineMillis(draft.deadline) ?: 0) > now

    fun deadlineMillis(value: String): Long? {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ROOT).apply {
            isLenient = false
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val position = ParsePosition(0)
        return parser.parse(value, position)?.time?.takeIf { position.index == value.length }
    }

    fun localDeadline(value: String): String? {
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ROOT).apply { isLenient = false }
        val position = ParsePosition(0)
        val date = parser.parse(value, position) ?: return null
        return if (position.index != value.length) null else
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ROOT).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date(date.time))
    }

    fun evidenceLink(value: String): Boolean = runCatching {
        val uri = URI(value)
        val hosts = setOf("youtube.com", "youtu.be", "twitch.tv", "clips.twitch.tv", "medal.tv", "streamable.com")
        val host = uri.host?.lowercase(Locale.ROOT)?.trimEnd('.')
            ?.removePrefix("www.")?.removePrefix("m.")
        value.length in 8..2048 && uri.scheme.equals("https", ignoreCase = true) && uri.userInfo == null &&
            (uri.port == -1 || uri.port == 443) &&
            host in hosts
    }.getOrDefault(false)

    private val MoneyPattern = Regex("(?i)(\\b(?:mxn|usd|eur(?:os?)?|pesos?|d[oó]lar(?:es)?)\\b|[$€£¥])")
}
