package com.pactogg.app.feature.connected.presentation

import com.pactogg.app.feature.connected.domain.Challenge
import com.pactogg.app.feature.connected.domain.Evidence
import com.pactogg.app.feature.connected.domain.ProductRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.update

/** A failed read must never turn an acknowledged upload into a request to upload again. */
internal suspend fun ConnectedViewModel.confirmEvidence(
    products: ProductRepository,
    contract: Challenge,
    evidence: Evidence,
    confirmation: String,
) {
    val accountId = mutable.value.account?.id ?: return
    mutable.update { current ->
        if (current.contract?.id == contract.id && current.account?.id == accountId) {
            current.copy(evidence = evidence, evidenceItems = (current.evidenceItems + evidence).distinctBy { it.id },
                link = "", contractStateStale = true, notice = confirmation,
                social = current.social.copy(contracts = ListState()))
        } else current
    }
    val refreshed = runCatching { products.challenge(contract) }.getOrElse { error ->
        if (error is CancellationException) throw error
        null
    }
    mutable.update { current ->
        if (current.contract?.id != contract.id || current.account?.id != accountId) current
        else if (refreshed != null) current.copy(contract = refreshed, contractStateStale = false)
        else current.copy(notice = "$confirmation No se pudo consultar el estado actualizado del reto. " +
            "No repitas el envío.")
    }
    if (mutable.value.contract?.id == contract.id && mutable.value.account?.id == accountId) {
        refreshEvidenceAfterUpload(contract)
    }
}
