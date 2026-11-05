package com.pactogg.app.feature.connected.presentation

import com.pactogg.app.feature.connected.domain.Challenge
import com.pactogg.app.feature.connected.domain.Group

fun canAccept(contract: Challenge, accountId: String?, group: Group?, stale: Boolean): Boolean =
    contractActionAllowed(contract, accountId, stale) && futureDeadline(contract.deadline) &&
        contract.challengerId != accountId &&
        (contract.targetType == "group" || (contract.targetType == "member" && contract.targetId == accountId)) &&
        group?.id == contract.groupId && group.points >= contract.bond
fun canReject(contract: Challenge, accountId: String?, stale: Boolean): Boolean =
    contractActionAllowed(contract, accountId, stale) && contract.targetType == "member" &&
        contract.targetId == accountId
private fun contractActionAllowed(contract: Challenge, accountId: String?, stale: Boolean): Boolean =
    accountId != null && !stale && contract.status == "open" && contract.version != null &&
        contract.challengerId != null

private fun futureDeadline(value: String): Boolean = runCatching {
    val normalized = value.replace(Regex("\\.\\d+(?=Z$|[+-]\\d{2}:\\d{2}$)"), "")
    val parser = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", java.util.Locale.ROOT)
        .apply { isLenient = false }
    val position = java.text.ParsePosition(0)
    val time = parser.parse(normalized, position)?.time ?: return@runCatching false
    position.index == normalized.length && time > System.currentTimeMillis()
}.getOrDefault(false)
