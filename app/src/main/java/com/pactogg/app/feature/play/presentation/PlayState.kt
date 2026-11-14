package com.pactogg.app.feature.play.presentation

import com.pactogg.app.feature.connected.data.PreparedPhoto
import com.pactogg.app.feature.connected.domain.Challenge
import com.pactogg.app.feature.connected.domain.Evidence
import com.pactogg.app.feature.play.domain.Commitment
import com.pactogg.app.feature.play.domain.FameEntry
import com.pactogg.app.feature.play.domain.NextMatch
import com.pactogg.app.feature.play.domain.Notice
import com.pactogg.app.feature.play.domain.PendingVote
import com.pactogg.app.feature.play.domain.PlayMatch
import com.pactogg.app.feature.play.domain.Ranking
import com.pactogg.app.feature.play.domain.ShameEntry
import com.pactogg.app.feature.play.domain.TournamentDetail
import com.pactogg.app.feature.play.domain.VoiceDraft
import com.pactogg.app.feature.play.domain.VoteResult
import com.pactogg.app.feature.play.domain.VoteSummary

/** Formulario de "Nuevo torneo" (maqueta `torneo/nuevo`). */
data class TournamentForm(val game: String = "", val maxGroups: String = "4", val entryPoints: String = "0",
    val date: String = "")

/** Estado de jurado, avisos, ranking, torneos y voz. Vive dentro de ConnectedState. */
data class PlayState(
    // Jurado
    val pendingVotes: List<PendingVote> = emptyList(),
    val votesLoaded: Boolean = false,
    val voteContract: Challenge? = null,
    val voteEvidence: List<Evidence> = emptyList(),
    val voteSummary: VoteSummary? = null,
    val voteResult: VoteResult? = null,
    val voteChoice: String? = null,
    val voteComment: String = "",
    val voteKey: String? = null,
    val confirmVote: Boolean = false,
    // Avisos
    val notices: List<Notice> = emptyList(),
    val noticesLoaded: Boolean = false,
    val unread: Int = 0,
    val noticesCursor: String? = null,
    val noticesMore: Boolean = false,
    // Palabra y ranking
    val standingsTab: String = "ranking",
    val ranking: Ranking? = null,
    val iOwe: List<Commitment> = emptyList(),
    val owedToMe: List<Commitment> = emptyList(),
    val commitmentsLoaded: Boolean = false,
    val fame: List<FameEntry>? = null,
    val shame: List<ShameEntry>? = null,
    // Torneos
    val nextMatch: NextMatch? = null,
    val tournament: TournamentDetail? = null,
    val match: PlayMatch? = null,
    val form: TournamentForm = TournamentForm(),
    val joinCode: String = "",
    val joinKey: String? = null,
    val scoreFirst: String = "",
    val scoreSecond: String = "",
    val reportPhoto: PreparedPhoto? = null,
    val disputeReason: String = "",
    // Voz
    val phrase: String = "",
    val voiceDraft: VoiceDraft? = null,
    val voiceUnavailable: Boolean = false,
)
