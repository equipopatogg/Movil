package com.pactogg.app.feature.connected.domain

import com.pactogg.app.feature.auth.domain.Account

data class Profile(val account: Account, val avatar: String, val emailVerified: Boolean,
    val completed: Int, val cheated: Int, val streak: Int, val trophies: Int, val wordIndex: Int,
    val groupsCount: Int)
data class Member(val id: String, val nickname: String, val avatar: String, val role: String,
    val points: Int, val joinedAt: String)
data class Page<T>(val items: List<T>, val hasMore: Boolean, val cursor: String?)
data class BoardFilter(val status: String? = null, val mine: Boolean = false,
    val game: String = "", val query: String = "")
data class NicknameAvailability(val nickname: String, val available: Boolean)

interface SocialRepository {
    suspend fun profile(): Profile
    suspend fun updateProfile(nickname: String): Profile
    suspend fun nickname(value: String): NicknameAvailability
    suspend fun groups(query: String, cursor: String?): Page<Group>
    suspend fun group(id: String): Group
    suspend fun join(code: String): Group
    suspend fun members(id: String, query: String, cursor: String?): Page<Member>
    suspend fun contracts(id: String, filter: BoardFilter, cursor: String?): Page<Challenge>
    suspend fun accept(contract: Challenge): Challenge
    suspend fun reject(contract: Challenge): Challenge
}
