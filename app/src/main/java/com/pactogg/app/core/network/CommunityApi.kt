package com.pactogg.app.core.network

import com.pactogg.app.feature.community.data.CastVoteRequest
import com.pactogg.app.feature.community.data.CastVoteResponse
import com.pactogg.app.feature.community.data.CommitmentResponse
import com.pactogg.app.feature.community.data.ContractResultResponse
import com.pactogg.app.feature.community.data.FameEntryResponse
import com.pactogg.app.feature.community.data.MyPointsResponse
import com.pactogg.app.feature.community.data.NotificationResponse
import com.pactogg.app.feature.community.data.PendingVoteResponse
import com.pactogg.app.feature.community.data.RankingEntryResponse
import com.pactogg.app.feature.community.data.ReadAllResponse
import com.pactogg.app.feature.community.data.ResolveVotingRequest
import com.pactogg.app.feature.community.data.VoteSummaryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CommunityApi : VotingApi, PointsCommitmentsApi, NoticesFameApi

interface VotingApi {
    @GET("api/v1/me/votes/pending")
    suspend fun pendingVotes(
        @Header("Authorization") bearer: String,
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int = 20
    ): Response<Envelope<List<PendingVoteResponse>>>

    @PUT("api/v1/contracts/{id}/votes")
    suspend fun castVote(
        @Header("Authorization") bearer: String,
        @Path("id") id: String,
        @Body body: CastVoteRequest,
        @Header("Idempotency-Key") operationKey: String
    ): Response<Envelope<CastVoteResponse>>

    @GET("api/v1/contracts/{id}/votes/summary")
    suspend fun voteSummary(
        @Header("Authorization") bearer: String,
        @Path("id") id: String
    ): Response<Envelope<VoteSummaryResponse>>

    @POST("api/v1/contracts/{id}/votes/resolve")
    suspend fun resolveVotes(
        @Header("Authorization") bearer: String,
        @Path("id") id: String,
        @Body body: ResolveVotingRequest,
        @Header("Idempotency-Key") operationKey: String
    ): Response<Envelope<ContractResultResponse>>

    @GET("api/v1/contracts/{id}/result")
    suspend fun contractResult(
        @Header("Authorization") bearer: String,
        @Path("id") id: String
    ): Response<Envelope<ContractResultResponse>>

}

interface PointsCommitmentsApi {
    @GET("api/v1/groups/{gid}/points/me")
    suspend fun myPoints(
        @Header("Authorization") bearer: String,
        @Path("gid") gid: String,
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int = 20,
        @Query("type") type: String? = null
    ): Response<Envelope<MyPointsResponse>>

    @GET("api/v1/groups/{gid}/points/ranking")
    suspend fun pointsRanking(
        @Header("Authorization") bearer: String,
        @Path("gid") gid: String,
        @Query("limit") limit: Int = 20
    ): Response<Envelope<List<RankingEntryResponse>>>

    @GET("api/v1/groups/{gid}/commitments")
    suspend fun commitments(
        @Header("Authorization") bearer: String,
        @Path("gid") gid: String,
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int = 20,
        @Query("role") role: String? = null,
        @Query("status") status: String? = null
    ): Response<Envelope<List<CommitmentResponse>>>

    @POST("api/v1/commitments/{id}/complete")
    suspend fun completeCommitment(
        @Header("Authorization") bearer: String,
        @Path("id") id: String
    ): Response<Envelope<CommitmentResponse>>

    @POST("api/v1/commitments/{id}/request-complete")
    suspend fun requestCompletion(
        @Header("Authorization") bearer: String,
        @Path("id") id: String
    ): Response<Envelope<CommitmentResponse>>

}

interface NoticesFameApi {
    @GET("api/v1/notifications")
    suspend fun notifications(
        @Header("Authorization") bearer: String,
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int = 20,
        @Query("unread") unread: Boolean = false
    ): Response<Envelope<List<NotificationResponse>>>

    @PATCH("api/v1/notifications/{id}/read")
    suspend fun readNotification(
        @Header("Authorization") bearer: String,
        @Path("id") id: String
    ): Response<Envelope<NotificationResponse>>

    @POST("api/v1/notifications/read-all")
    suspend fun readAllNotifications(
        @Header("Authorization") bearer: String
    ): Response<Envelope<ReadAllResponse>>

    @GET("api/v1/groups/{gid}/fame")
    suspend fun fame(
        @Header("Authorization") bearer: String,
        @Path("gid") gid: String,
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int = 20
    ): Response<Envelope<List<FameEntryResponse>>>

}
