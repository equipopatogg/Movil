package com.pactogg.app.feature.connected.presentation

import com.pactogg.app.feature.connected.data.NicknamePattern
import kotlinx.coroutines.flow.update

fun ConnectedViewModel.joinCode(value: String) {
    if (!mutable.value.busy) mutable.update { it.copy(social = it.social.copy(joinCode = value.take(6))) }
}
fun ConnectedViewModel.joinGroup() {
    val code = mutable.value.social.joinCode.trim()
    if (!code.matches(Regex("[A-Za-z0-9]{6}"))) {
        mutable.update { it.copy(error = "Escribe los seis caracteres del código de invitación.") }
        return
    }
    perform {
        val group = socials.join(code)
        applyGroup(group)
        mutable.update { it.copy(social = it.social.copy(joinCode = "", groups = ListState()),
            notice = "Te uniste al grupo confirmado por el servidor.") }
    }
}
fun ConnectedViewModel.editNickname(value: String) {
    if (mutable.value.busy) return
    mutable.update { it.copy(social = it.social.copy(editedNickname = value.take(24))) }
    checkNickname(value.take(24))
}
fun ConnectedViewModel.saveProfile() {
    val nickname = mutable.value.social.editedNickname.trim()
    if (!NicknamePattern.matches(nickname)) {
        mutable.update { it.copy(error = "El apodo debe tener 3 a 24 caracteres permitidos.") }
        return
    }
    cancelNicknameCheck()
    perform {
        val profile = socials.updateProfile(nickname)
        mutable.update { it.copy(account = profile.account, social = it.social.copy(profile = profile,
            editedNickname = profile.account.nickname, members = ListState()), notice = "Apodo actualizado.") }
    }
}
fun ConnectedViewModel.targetMember(id: String?) {
    val current = mutable.value
    if (current.busy || id == current.account?.id) return
    if (id != null && current.social.members.items.none { it.id == id }) return
    mutable.update { it.copy(draft = it.draft.copy(targetId = id), reviewing = false,
        social = it.social.copy(selectedRival = it.social.members.items.firstOrNull { member -> member.id == id })) }
}
fun ConnectedViewModel.requestContractAction(action: String) {
    val current = mutable.value
    val contract = current.contract ?: return
    val allowed = when (action) {
        "accept" -> canAccept(contract, current.account?.id, current.group, current.contractStateStale)
        "reject" -> canReject(contract, current.account?.id, current.contractStateStale)
        else -> false
    }
    if (!current.busy && allowed) perform {
        val fresh = products.challenge(contract)
        val group = socials.group(fresh.groupId)
        mutable.update { it.copy(contract = fresh, group = group, contractStateStale = false,
            social = it.social.copy(pendingAction = action)) }
    }
}
fun ConnectedViewModel.cancelContractAction() {
    if (!mutable.value.busy) mutable.update { it.copy(social = it.social.copy(pendingAction = null)) }
}
fun ConnectedViewModel.confirmContractAction() {
    val current = mutable.value
    val contract = current.contract ?: return
    val action = current.social.pendingAction ?: return
    val allowed = if (action == "accept") canAccept(contract, current.account?.id, current.group,
        current.contractStateStale) else canReject(contract, current.account?.id, current.contractStateStale)
    if (!allowed) cancelContractAction()
    else perform {
        // Clear confirmation before sending: an uncertain write requires a new detail read and decision.
        mutable.update { it.copy(contractStateStale = true, social = it.social.copy(pendingAction = null)) }
        val result = if (action == "accept") socials.accept(contract) else socials.reject(contract)
        mutable.update { it.copy(contract = result, contractStateStale = false,
            group = it.group?.let { group -> result.myPoints?.let { points -> group.copy(points = points) } ?: group },
            social = it.social.copy(contracts = ListState(), groups = ListState(),
                members = ListState(), profile = null),
            notice = if (action == "accept") "Reto aceptado; saldo confirmado por el servidor."
            else "Reto rechazado.") }
    }
}

