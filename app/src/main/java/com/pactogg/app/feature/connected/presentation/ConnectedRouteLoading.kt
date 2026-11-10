package com.pactogg.app.feature.connected.presentation

internal fun ConnectedViewModel.loadRoute(route: ConnectedRoute) {
    when (route) {
        ConnectedRoute.TOURNAMENTS -> if (!mutable.value.tournamentsLoaded) loadTournaments()
        ConnectedRoute.PROFILE -> loadProfile()
        ConnectedRoute.GROUPS -> loadGroups()
        ConnectedRoute.MEMBERS -> loadMembers()
        ConnectedRoute.BOARD -> loadBoard()
        ConnectedRoute.EVIDENCE -> loadEvidence()
        ConnectedRoute.JURY, ConnectedRoute.VOTING, ConnectedRoute.POINTS, ConnectedRoute.RANKING,
        ConnectedRoute.COMMITMENTS, ConnectedRoute.NOTIFICATIONS, ConnectedRoute.FAME -> loadCommunity()
        else -> Unit
    }
}

internal fun evidenceBackRoute(state: ConnectedState, destination: ConnectedRoute): ConnectedRoute =
    if (destination != ConnectedRoute.EVIDENCE) state.evidenceReturnRoute
    else if (state.route == ConnectedRoute.VOTING) ConnectedRoute.VOTING else ConnectedRoute.CONTRACT
