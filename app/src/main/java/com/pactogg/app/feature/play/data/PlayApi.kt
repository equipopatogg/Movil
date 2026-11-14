package com.pactogg.app.feature.play.data

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

private const val Auth = "Authorization"
private const val Idem = "Idempotency-Key"

/** Rutas de la API 0.12.x para jurado, avisos, ranking, torneos y voz (specs MO-VOT, MO-NOT, MO-RANK,
 * MO-TOR-JUGAR, MO-CON-08). */
@Suppress("TooManyFunctions")
interface PlayApi {
    // Jurado
    @GET("api/v1/me/votes/pending")
    suspend fun pendingVotes(@Header(Auth) bearer: String,
        @Query("limit") limit: Int = 50): Response<PlayEnvelope<List<PendingVoteDto>>>

    @GET("api/v1/contracts/{id}/votes/summary")
    suspend fun voteSummary(@Header(Auth) bearer: String,
        @Path("id") id: String): Response<PlayEnvelope<VoteSummaryDto>>

    @PUT("api/v1/contracts/{id}/votes")
    suspend fun castVote(@Header(Auth) bearer: String, @Header(Idem) key: String, @Path("id") id: String,
        @Body body: CastVoteRequest): Response<PlayEnvelope<CastVoteDto>>

    @GET("api/v1/contracts/{id}/result")
    suspend fun voteResult(@Header(Auth) bearer: String, @Path("id") id: String): Response<PlayEnvelope<VoteResultDto>>

    @POST("api/v1/contracts/{id}/votes/resolve")
    suspend fun resolveVoting(@Header(Auth) bearer: String, @Header(Idem) key: String, @Path("id") id: String,
        @Body body: ResolveVotingRequest): Response<PlayEnvelope<VoteResultDto>>

    // Avisos
    @GET("api/v1/notifications")
    suspend fun notices(@Header(Auth) bearer: String, @Query("unread") unread: Boolean?,
        @Query("cursor") cursor: String?, @Query("limit") limit: Int = 30): Response<PlayEnvelope<List<NoticeDto>>>

    @PATCH("api/v1/notifications/{id}/read")
    suspend fun markRead(@Header(Auth) bearer: String, @Path("id") id: String): Response<PlayEnvelope<NoticeDto>>

    @POST("api/v1/notifications/read-all")
    suspend fun markAllRead(@Header(Auth) bearer: String): Response<Unit>

    @DELETE("api/v1/notifications/{id}")
    suspend fun deleteNotice(@Header(Auth) bearer: String, @Path("id") id: String): Response<Unit>

    // Palabra y ranking
    @GET("api/v1/groups/{gid}/ranking")
    suspend fun ranking(@Header(Auth) bearer: String, @Path("gid") gid: String,
        @Query("limit") limit: Int = 50): Response<PlayEnvelope<RankingDto>>

    @GET("api/v1/groups/{gid}/commitments")
    suspend fun commitments(@Header(Auth) bearer: String, @Path("gid") gid: String, @Query("role") role: String,
        @Query("limit") limit: Int = 50): Response<PlayEnvelope<List<CommitmentDto>>>

    @GET("api/v1/groups/{gid}/commitments/summary")
    suspend fun commitmentSummary(@Header(Auth) bearer: String,
        @Path("gid") gid: String): Response<PlayEnvelope<CommitmentSummaryDto>>

    @POST("api/v1/commitments/{id}/complete")
    suspend fun completeCommitment(@Header(Auth) bearer: String,
        @Path("id") id: String): Response<PlayEnvelope<CommitmentDto>>

    @POST("api/v1/commitments/{id}/request-complete")
    suspend fun requestCompletion(@Header(Auth) bearer: String,
        @Path("id") id: String): Response<PlayEnvelope<CommitmentDto>>

    @GET("api/v1/groups/{gid}/fame")
    suspend fun fame(@Header(Auth) bearer: String, @Path("gid") gid: String,
        @Query("limit") limit: Int = 30): Response<PlayEnvelope<List<FameDto>>>

    @GET("api/v1/groups/{gid}/shame")
    suspend fun shame(@Header(Auth) bearer: String, @Path("gid") gid: String,
        @Query("limit") limit: Int = 30): Response<PlayEnvelope<List<ShameDto>>>

    // Torneos
    @POST("api/v1/groups/{gid}/tournaments")
    suspend fun createTournament(@Header(Auth) bearer: String, @Path("gid") gid: String,
        @Body body: CreateTournamentRequest): Response<PlayEnvelope<TournamentDetailDto>>

    @GET("api/v1/tournaments/{id}")
    suspend fun tournament(@Header(Auth) bearer: String,
        @Path("id") id: String): Response<PlayEnvelope<TournamentDetailDto>>

    @POST("api/v1/tournaments/{id}/join")
    suspend fun joinTournament(@Header(Auth) bearer: String, @Header(Idem) key: String, @Path("id") id: String,
        @Body body: JoinTournamentRequest): Response<PlayEnvelope<TournamentDetailDto>>

    @DELETE("api/v1/tournaments/{id}/join")
    suspend fun leaveTournament(@Header(Auth) bearer: String, @Path("id") id: String,
        @Query("group_id") groupId: String): Response<Unit>

    @POST("api/v1/tournaments/{id}/start")
    suspend fun startTournament(@Header(Auth) bearer: String,
        @Path("id") id: String): Response<PlayEnvelope<TournamentDetailDto>>

    @GET("api/v1/matches/{id}")
    suspend fun match(@Header(Auth) bearer: String, @Path("id") id: String): Response<PlayEnvelope<PlayMatchDto>>

    @GET("api/v1/me/matches/next")
    suspend fun nextMatch(@Header(Auth) bearer: String): Response<NextMatchEnvelope>

    @Multipart
    @POST("api/v1/matches/{id}/report")
    suspend fun reportMatch(@Header(Auth) bearer: String, @Path("id") id: String, @Part file: MultipartBody.Part,
        @Part("p1_score") p1: RequestBody, @Part("p2_score") p2: RequestBody,
        @Part("group_id") groupId: RequestBody?): Response<PlayEnvelope<PlayMatchDto>>

    @POST("api/v1/matches/{id}/confirm")
    suspend fun confirmMatch(@Header(Auth) bearer: String, @Path("id") id: String): Response<PlayEnvelope<PlayMatchDto>>

    @POST("api/v1/matches/{id}/dispute")
    suspend fun disputeMatch(@Header(Auth) bearer: String, @Path("id") id: String,
        @Body body: DisputeRequest): Response<PlayEnvelope<PlayMatchDto>>

    @POST("api/v1/matches/{id}/resolve")
    suspend fun resolveMatch(@Header(Auth) bearer: String, @Path("id") id: String,
        @Body body: ResolveMatchRequest): Response<PlayEnvelope<PlayMatchDto>>

    // Voz
    @POST("api/v1/groups/{gid}/contracts/parse")
    suspend fun parsePhrase(@Header(Auth) bearer: String, @Path("gid") gid: String,
        @Body body: ParseRequest): Response<PlayEnvelope<VoiceDraftDto>>
}
