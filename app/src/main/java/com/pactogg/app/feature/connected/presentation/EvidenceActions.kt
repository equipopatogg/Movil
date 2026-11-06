package com.pactogg.app.feature.connected.presentation

import com.pactogg.app.core.network.ApiFailure
import com.pactogg.app.feature.connected.data.PreparedPhoto
import com.pactogg.app.feature.connected.domain.Challenge
import com.pactogg.app.feature.connected.domain.EvidenceRules
import com.pactogg.app.feature.connected.domain.InputRules
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.update

internal fun ConnectedState.canUploadEvidence(): Boolean = contract?.let {
    !contractStateStale && evidenceBlockedId != it.id && EvidenceRules.canUpload(it, account?.id)
} == true

fun ConnectedViewModel.preparePhoto(read: suspend () -> PreparedPhoto) {
    val current = state.value
    if (!current.canUploadEvidence()) return
    perform {
        mutable.value.selectedPhoto?.clear()
        mutable.update { it.copy(selectedPhoto = null) }
        val photo = read()
        if (state.value.account?.id == current.account?.id && state.value.contract?.id == current.contract?.id) {
            mutable.value.selectedPhoto?.clear()
            mutable.update { it.copy(selectedPhoto = photo) }
        } else photo.clear()
    }
}

fun ConnectedViewModel.discardPhoto() {
    if (state.value.busy) return
    mutable.value.selectedPhoto?.clear()
    mutable.update { it.copy(selectedPhoto = null) }
}

fun ConnectedViewModel.uploadSelectedPhoto() {
    val photo = state.value.selectedPhoto ?: return
    sendPhoto { photo.bytes to photo.mimeType }
}

internal fun ConnectedViewModel.sendEvidenceLink() {
    val current = state.value
    val contract = current.contract ?: return
    if (!current.canUploadEvidence()) return
    if (!InputRules.evidenceLink(current.link)) {
        mutable.update { it.copy(error = "Usa un enlace HTTPS de YouTube, Twitch, Medal o Streamable.") }
    } else perform {
        evidenceUpload(contract) {
            val evidence = products.sendLink(contract, current.link)
            confirmEvidence(products, contract, evidence, "Enlace enviado. Ya está disponible como evidencia.")
        }
    }
}

fun ConnectedViewModel.sendPhoto(read: suspend () -> Pair<ByteArray, String>) {
    val current = state.value
    val contract = current.contract ?: return
    if (!current.canUploadEvidence()) return
    perform {
        val (bytes, mime) = read()
        try {
            evidenceUpload(contract) {
                val evidence = products.sendPhoto(contract, bytes, mime)
                confirmEvidence(products, contract, evidence, "Foto enviada. Ya está disponible como evidencia.")
            }
        } finally {
            bytes.fill(0)
            mutable.update { it.copy(selectedPhoto = null) }
        }
    }
}

/** An ambiguous upload requires a fresh read before the next explicit send. */
private suspend fun ConnectedViewModel.evidenceUpload(contract: Challenge, send: suspend () -> Unit) {
    mutable.update { it.copy(contractStateStale = true) }
    try { send() } catch (error: ApiFailure) {
        mutable.update { it.copy(contractStateStale = error.status !in 400..499,
            evidenceBlockedId = contract.id.takeIf { error.status == 409 ||
                (error.status == 403 && error.code == "NOT_CONTRACT_TARGET") } ?: it.evidenceBlockedId) }
        throw error
    }
}

fun ConnectedViewModel.loadEvidence() {
    val current = state.value
    val contract = current.contract ?: return
    perform {
        val items = try { products.evidence(contract) } catch (error: ApiFailure) {
            if (error.status != 405) throw error
            null
        }
        if (state.value.account?.id == current.account?.id && state.value.contract?.id == contract.id) {
            mutable.update { if (items != null) it.copy(evidenceItems = items,
                evidenceLoaded = true, evidenceUnavailable = false)
            else it.copy(evidenceLoaded = false, evidenceUnavailable = true) }
        }
    }
}

internal suspend fun ConnectedViewModel.refreshEvidenceAfterUpload(contract: Challenge) {
    val accountId = state.value.account?.id
    val items = runCatching { products.evidence(contract) }.getOrElse { error ->
        if (error is CancellationException) throw error
        null
    }
    if (state.value.account?.id != accountId || state.value.contract?.id != contract.id) return
    mutable.update { current ->
        if (items != null) current.copy(evidenceItems = (items + listOfNotNull(current.evidence))
            .distinctBy { it.id }, evidenceLoaded = true, evidenceUnavailable = false)
        else current.copy(evidenceLoaded = false,
            notice = "Tu evidencia se envió. Pulsa Actualizar evidencias para ver las fotos. No la envíes otra vez.")
    }
}
