package com.pactogg.app.feature.connected.presentation

import com.pactogg.app.feature.auth.domain.Account
import com.pactogg.app.feature.auth.presentation.AccessState
import com.pactogg.app.feature.connected.domain.Challenge
import com.pactogg.app.feature.connected.domain.ChallengeDraft
import com.pactogg.app.feature.connected.domain.Evidence
import com.pactogg.app.feature.connected.domain.Group
import com.pactogg.app.feature.connected.domain.Tournament

enum class ConnectedRoute { HOME, GROUPS, MEMBERS, BOARD, CREATE, CONTRACT, EVIDENCE, TOURNAMENTS, PROFILE }

data class FailureDiagnostic(val status: Int, val code: String, val requestId: String?)

data class ConnectedState(
    val social: SocialState = SocialState(),
    val restoring: Boolean = true,
    val account: Account? = null,
    val access: AccessState = AccessState(),
    val route: ConnectedRoute = ConnectedRoute.HOME,
    val busy: Boolean = false,
    val error: String? = null,
    val diagnostic: FailureDiagnostic? = null,
    val notice: String? = null,
    val groupName: String = "",
    val group: Group? = null,
    val draft: ChallengeDraft = ChallengeDraft(),
    val deadlineLocal: String = "",
    val reviewing: Boolean = false,
    val contract: Challenge? = null,
    val contractStateStale: Boolean = false,
    val link: String = "",
    val evidence: Evidence? = null,
    val tournaments: List<Tournament> = emptyList(),
    val tournamentsLoaded: Boolean = false,
    val status: String? = null,
    val gameFilter: String = "",
    val nextCursor: String? = null,
    val hasMore: Boolean = false,
    val selectedTournament: Tournament? = null,
)
