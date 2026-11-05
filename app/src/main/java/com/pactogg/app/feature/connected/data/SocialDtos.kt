package com.pactogg.app.feature.connected.data

import com.pactogg.app.feature.auth.domain.Account
import com.pactogg.app.feature.connected.domain.Member
import com.pactogg.app.feature.connected.domain.Profile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileStatsDto(val completed: Int, val cheated: Int, val streak: Int,
    val trophies: Int, @SerialName("word_index") val wordIndex: Int)
@Serializable
data class ProfileDto(val id: String, val email: String, val nickname: String,
    @SerialName("avatar_id") val avatar: String, @SerialName("email_verified") val verified: Boolean,
    val stats: ProfileStatsDto, @SerialName("groups_count") val groups: Int) {
    fun toDomain(): Profile {
        require(id.matches(SocialId) && email.isNotBlank() && nickname.isNotBlank() && groups >= 0)
        require(stats.completed >= 0 && stats.cheated >= 0 && stats.streak >= 0 && stats.trophies >= 0)
        require(stats.wordIndex in 0..100)
        return Profile(Account(id, email, nickname), avatar, verified, stats.completed, stats.cheated,
            stats.streak, stats.trophies, stats.wordIndex, groups)
    }
}
@Serializable
data class ProfilePatch(val nickname: String)
@Serializable
data class NicknameDto(val nickname: String, val available: Boolean)
@Serializable
data class JoinRequest(val code: String)
@Serializable
data class ContractAction(val version: Int)
@Serializable
data class MemberDto(@SerialName("user_id") val id: String, val nickname: String,
    @SerialName("avatar_id") val avatar: String, val role: String,
    @SerialName("points_balance") val points: Int, @SerialName("joined_at") val joinedAt: String) {
    fun toDomain(): Member {
        require(id.matches(SocialId) && nickname.isNotBlank() && role in listOf("admin", "member"))
        require(points >= 0 && joinedAt.isNotBlank())
        return Member(id, nickname, avatar, role, points, joinedAt)
    }
}
internal val SocialId = Regex("[a-fA-F0-9]{24}")
