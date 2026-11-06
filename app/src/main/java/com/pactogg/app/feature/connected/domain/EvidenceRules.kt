package com.pactogg.app.feature.connected.domain

import java.net.URI
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Locale

object EvidenceRules {
    fun canUpload(contract: Challenge, accountId: String?, now: Long = System.currentTimeMillis()): Boolean {
        val executor = contract.executorId ?: contract.acceptedBy ?: contract.targetId.takeIf {
            contract.targetType == "member"
        }
        return accountId != null && executor == accountId &&
            contract.status in setOf("in_progress", "voting") &&
            (contract.evidenceCount ?: 0) < 3 && (timestamp(contract.deadline) ?: 0) > now
    }

    fun imageUrl(value: String?): String? = value?.takeIf {
        runCatching {
            val uri = URI(it)
            uri.scheme == "https" && !uri.host.isNullOrBlank() && uri.userInfo == null &&
                (uri.port == -1 || uri.port == 443) && uri.fragment == null
        }.getOrDefault(false)
    }

    fun expired(value: String?, now: Long = System.currentTimeMillis()): Boolean =
        value != null && (timestamp(value) ?: 0) <= now

    private fun timestamp(value: String): Long? {
        val normalized = value.replace(Regex("\\.\\d+(?=Z$|[+-]\\d{2}:\\d{2}$)"), "")
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.ROOT).apply { isLenient = false }
        val position = ParsePosition(0)
        return parser.parse(normalized, position)?.time?.takeIf { position.index == normalized.length }
    }
}
