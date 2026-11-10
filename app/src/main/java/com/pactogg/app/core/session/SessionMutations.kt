package com.pactogg.app.core.session

import com.pactogg.app.feature.auth.data.SessionDto
import com.pactogg.app.feature.auth.domain.Session

/** Reuses the generation guard and encrypted store owned by SessionManager. */
internal class SessionMutations(
    val commit: suspend (suspend () -> SessionDto) -> Unit,
    val clear: suspend (Session?) -> Unit,
)
