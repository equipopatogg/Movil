package com.pactogg.app.core.navigation

import androidx.annotation.StringRes
import com.pactogg.app.R

enum class Destination(val route: String, @param:StringRes val label: Int, val mark: String) {
    CONTRACTS("contracts", R.string.tab_contracts, "R"),
    TOURNAMENTS("tournaments", R.string.tab_tournaments, "T"),
    COMMITMENTS("commitments", R.string.tab_commitments, "C"),
    PROFILE("profile", R.string.tab_profile, "P"),
}

internal const val GROUPS_ROUTE = "groups"
