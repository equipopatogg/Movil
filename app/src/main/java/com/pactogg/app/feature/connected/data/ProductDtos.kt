package com.pactogg.app.feature.connected.data

import com.pactogg.app.feature.connected.domain.Challenge
import com.pactogg.app.feature.connected.domain.Evidence
import com.pactogg.app.feature.connected.domain.Group
import com.pactogg.app.feature.connected.domain.Match
import com.pactogg.app.feature.connected.domain.Tournament
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupRequest(val name: String)

@Serializable
data class SettingsDto(@SerialName("bond_options") val bonds: List<Int>)

@Serializable
data class GroupDto(val id: String, val name: String, @SerialName("invite_code") val code: String,
    @SerialName("my_role") val role: String, @SerialName("my_points") val points: Int,
    val settings: SettingsDto) {
    fun toDomain(): Group {
        require(id.matches(IdPattern) && name.isNotBlank() && code.length == 6 && role.isNotBlank())
        require(settings.bonds.isNotEmpty() && settings.bonds.all { it >= 0 })
        return Group(id, name, code, role, points, settings.bonds)
    }
}

@Serializable
data class ContractRequest(@SerialName("group_id") val groupId: String, val game: String,
    val condition: String, val reward: String, val deadline: String,
    @SerialName("bond_required") val bond: Int,
    @SerialName("target_type") val targetType: String = "group", val source: String = "manual",
    @SerialName("target_id") val targetId: String? = null)

@Serializable
data class ContractDto(val id: String, @SerialName("group_id") val groupId: String, val game: String,
    val condition: String, val reward: String, val deadline: String,
    @SerialName("bond_required") val bond: Int, val status: String,
    @SerialName("challenger_id") val challengerId: String? = null,
    @SerialName("target_type") val targetType: String? = null,
    @SerialName("target_id") val targetId: String? = null,
    @SerialName("accepted_by") val acceptedBy: String? = null,
    @SerialName("evidence_count") val evidenceCount: Int? = null, val version: Int? = null,
    @SerialName("closed_reason") val closedReason: String? = null,
    @SerialName("my_points") val myPoints: Int? = null,
    @SerialName("accepted_at") val acceptedAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("executor_id") val executorId: String? = null) {
    fun toDomain(): Challenge {
        require(id.matches(IdPattern) && groupId.matches(IdPattern) && status.isNotBlank())
        require(game.isNotBlank() && condition.isNotBlank() && reward.isNotBlank())
        require(bond >= 0 && (version == null || version >= 1) && (evidenceCount == null || evidenceCount >= 0))
        return Challenge(id, groupId, game, condition, reward, deadline, bond, status,
            challengerId, targetType, targetId, acceptedBy, evidenceCount, version, closedReason, myPoints,
            acceptedAt, updatedAt, createdAt, executorId)
    }
}

@Serializable
data class LinkRequest(val url: String, val type: String = "link")

@Serializable
data class ModerationDto(val status: String)

@Serializable
data class EvidenceDto(val id: String, @SerialName("contract_id") val contractId: String,
    val type: String, val moderation: ModerationDto, val url: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("expires_at") val expiresAt: String? = null) {
    fun toDomain(): Evidence {
        require(id.matches(IdPattern) && contractId.matches(IdPattern))
        require(type in listOf("link", "photo") && moderation.status == "approved")
        return Evidence(id, contractId, type, moderation.status, url, createdAt, expiresAt)
    }
}

@Serializable
data class MatchDto(val id: String, val round: Int, val position: Int,
    @SerialName("p1_group_id") val first: String? = null,
    @SerialName("p2_group_id") val second: String? = null,
    @SerialName("p1_score") val firstScore: Int? = null,
    @SerialName("p2_score") val secondScore: Int? = null, val status: String) {
    fun toDomain(): Match {
        require(id.matches(IdPattern) && round >= 0 && position >= 0)
        return Match(id, round, position, first, second, firstScore, secondScore, status)
    }
}

@Serializable
data class BracketDto(val matches: List<MatchDto>)

@Serializable
data class TournamentDto(val id: String, val game: String, val format: String, val status: String,
    @SerialName("scheduled_at") val scheduledAt: String, val bracket: BracketDto) {
    fun toDomain(): Tournament {
        require(id.matches(IdPattern) && game.isNotBlank() && status.isNotBlank())
        return Tournament(id, game, format, status, scheduledAt, bracket.matches.map { it.toDomain() })
    }
}

private val IdPattern = Regex("[a-fA-F0-9]{24}")
