package com.pactogg.app.feature.connected.presentation

import com.pactogg.app.feature.connected.domain.BoardFilter
import com.pactogg.app.feature.connected.domain.Challenge
import com.pactogg.app.feature.connected.domain.Group
import com.pactogg.app.feature.connected.domain.Member
import com.pactogg.app.feature.connected.domain.Page
import com.pactogg.app.feature.connected.domain.Profile

data class ListState<T>(val items: List<T> = emptyList(), val loaded: Boolean = false,
    val hasMore: Boolean = false, val cursor: String? = null) {
    fun merge(page: Page<T>, more: Boolean, id: (T) -> String): ListState<T> =
        ListState(((if (more) items else emptyList()) + page.items).distinctBy(id), true, page.hasMore, page.cursor)
}
data class SocialState(
    val profile: Profile? = null,
    val editedNickname: String = "",
    val nicknameNotice: String? = null,
    val groupQuery: String = "",
    val groups: ListState<Group> = ListState(),
    val joinCode: String = "",
    val selectedRival: Member? = null,
    val memberQuery: String = "",
    val members: ListState<Member> = ListState(),
    val boardFilter: BoardFilter = BoardFilter(),
    val contracts: ListState<Challenge> = ListState(),
    val pendingAction: String? = null,
)
