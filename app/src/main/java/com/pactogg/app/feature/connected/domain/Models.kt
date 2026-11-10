package com.pactogg.app.feature.connected.domain

data class Group(val id: String, val name: String, val inviteCode: String,
    val role: String, val points: Int, val bonds: List<Int>)

data class Challenge(val id: String, val groupId: String, val game: String,
    val condition: String, val reward: String, val deadline: String, val bond: Int, val status: String,
    val challengerId: String? = null, val targetType: String? = null, val targetId: String? = null,
    val acceptedBy: String? = null, val evidenceCount: Int? = null, val version: Int? = null,
    val closedReason: String? = null, val myPoints: Int? = null,
    val acceptedAt: String? = null, val updatedAt: String? = null, val createdAt: String? = null,
    val executorId: String? = null)

data class ChallengeDraft(val game: String = "", val condition: String = "", val reward: String = "",
    val deadline: String = "", val bond: Int? = null, val targetId: String? = null)

data class Evidence(val id: String, val contractId: String, val type: String, val moderation: String,
    val url: String? = null, val createdAt: String? = null, val expiresAt: String? = null)

data class Match(val id: String, val round: Int, val position: Int, val first: String?, val second: String?,
    val firstScore: Int?, val secondScore: Int?, val status: String)

data class Tournament(val id: String, val game: String, val format: String, val status: String,
    val scheduledAt: String, val matches: List<Match>)

data class TournamentPage(val items: List<Tournament>, val hasMore: Boolean, val nextCursor: String?)

interface ProductRepository {
    suspend fun createGroup(name: String): Group
    suspend fun createChallenge(group: Group, draft: ChallengeDraft): Challenge
    suspend fun createChallengeInGroup(group: Group, draft: ChallengeDraft): Challenge = createChallenge(group, draft)
    suspend fun challenge(contract: Challenge): Challenge
    suspend fun sendLink(contract: Challenge, url: String): Evidence
    suspend fun sendPhoto(contract: Challenge, bytes: ByteArray, mimeType: String): Evidence
    suspend fun evidence(contract: Challenge): List<Evidence>
    suspend fun tournaments(status: String?, game: String?, cursor: String?): TournamentPage
    suspend fun groupTournaments(groupId: String, status: String?, game: String?, cursor: String?): TournamentPage =
        tournaments(status, game, cursor)
}
