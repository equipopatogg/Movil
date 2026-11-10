package com.pactogg.app.feature.connected.presentation

internal fun ConnectedViewModel.returnFromMaintenance() {
    mutable.value = mutable.value.copy(contractStateStale = true, contract = null,
        evidenceItems = emptyList(), social = mutable.value.social.copy(contracts = ListState()))
    navigate(ConnectedRoute.BOARD)
}
