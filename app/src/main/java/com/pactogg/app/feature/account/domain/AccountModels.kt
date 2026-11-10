package com.pactogg.app.feature.account.domain

data class NotificationChoices(val challenges: Boolean, val votes: Boolean,
    val tournaments: Boolean, val commitments: Boolean)
data class MemberPreview(val id: String, val nickname: String, val wordIndex: Int,
    val completed: Int, val cheated: Int)
data class HistoryPage(val entries: List<String>, val cursor: String?)
