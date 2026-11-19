package com.pactogg.app.feature.play.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.pactogg.app.feature.connected.data.PhotoPreparer
import com.pactogg.app.feature.connected.data.PreparedPhoto
import com.pactogg.app.core.designsystem.ButtonKind
import com.pactogg.app.core.designsystem.Initial
import com.pactogg.app.core.designsystem.Kicker
import com.pactogg.app.core.designsystem.MakeButton
import com.pactogg.app.core.designsystem.MakeCard
import com.pactogg.app.core.designsystem.MakeField
import com.pactogg.app.core.designsystem.Segmented
import com.pactogg.app.core.designsystem.avatarTone
import com.pactogg.app.feature.connected.domain.Member
import com.pactogg.app.feature.connected.domain.Profile
import com.pactogg.app.feature.connected.presentation.ConnectedState
import com.pactogg.app.feature.connected.presentation.ConnectedViewModel
import com.pactogg.app.feature.play.domain.AvatarIds
import com.pactogg.app.feature.play.domain.ReportReasons
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.update

private const val DeleteWord = "ELIMINAR"

/** ID 22/23: cambia el color del avatar. */
/** USR-07: la foto se prepara en el teléfono (JPEG ≤ 5 MB) y el servidor la modera antes de guardarla. */
fun ConnectedViewModel.uploadAvatar(prepare: suspend () -> PreparedPhoto) = perform {
    val photo = prepare()
    try {
        val profile = play.avatarPhoto(photo.bytes, photo.mimeType)
        mutable.update { it.copy(social = it.social.copy(profile = profile), notice = "Foto de perfil actualizada.") }
    } finally {
        photo.clear()
    }
}

fun ConnectedViewModel.chooseAvatar(id: String) = perform {
    val profile = play.avatar(id)
    mutable.update { it.copy(social = it.social.copy(profile = profile), notice = "Avatar actualizado.") }
}

/** ID 29: elimina la cuenta en el servidor y borra la sesión de este teléfono. */
fun ConnectedViewModel.deleteAccount(password: String, confirm: String) {
    if (confirm.trim() != DeleteWord) {
        mutable.update { it.copy(error = "Escribe $DeleteWord en mayúsculas para confirmar.") }
        return
    }
    perform {
        play.deleteAccount(password.takeIf { it.isNotEmpty() })
        // La cuenta ya no existe: el cierre remoto puede fallar, pero la sesión local siempre se borra.
        runCatching { sessions.logout() }.exceptionOrNull()?.let { if (it is CancellationException) throw it }
    }
}

/** ID 149: reporta a un miembro ante el administrador del grupo. */
fun ConnectedViewModel.reportMember(member: Member, reason: String) {
    val group = state.value.group ?: return
    perform {
        play.reportUser(group.id, member.id, reason)
        mutable.update { it.copy(notice = "Reporte enviado. El administrador del grupo lo revisará.") }
    }
}

/** ID 153: bloquea a otra persona; ya no podrán retarse entre sí. */
fun ConnectedViewModel.blockMember(member: Member) = perform {
    play.blockUser(member.id)
    mutable.update { it.copy(notice = "Bloqueaste a ${member.nickname}. Ya no podrán retarse entre ustedes.") }
}

/** Selector de avatar del perfil: tres colores como en la maqueta. */
@Composable
fun AvatarPicker(profile: Profile, busy: Boolean, model: ConnectedViewModel) {
    val context = LocalContext.current
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) model.uploadAvatar { PhotoPreparer(context).prepare(uri) }
    }
    Kicker("Foto o color del avatar")
    MakeButton(if (profile.avatar == "photo") "Cambiar mi foto" else "Usar una foto", ButtonKind.Secondary,
        enabled = !busy) {
        picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
        AvatarIds.forEach { id ->
            Box(Modifier.clickable(enabled = !busy && profile.avatar != id, role = Role.Button) {
                model.chooseAvatar(id)
            }) { Initial(profile.account.nickname, avatarTone(id), if (profile.avatar == id) 46 else 34) }
        }
    }
}

/** Zona de eliminación de cuenta (requisito del docente y de Google Play). */
@Composable
fun DeleteAccountCard(state: ConnectedState, model: ConnectedViewModel) {
    var open by rememberSaveable { mutableStateOf(false) }
    var password by rememberSaveable { mutableStateOf("") }
    var confirm by rememberSaveable { mutableStateOf("") }
    if (!open) {
        MakeButton("Eliminar mi cuenta", ButtonKind.Ghost, enabled = !state.busy, icon = null) { open = true }
        return
    }
    MakeCard {
        Text("Eliminar mi cuenta", style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error)
        Text("Se borran tu perfil y tus datos personales. Los retos cerrados quedan en el historial del grupo " +
            "sin tu nombre. No se puede deshacer.", style = MaterialTheme.typography.bodySmall)
        OutlinedTextField(password, { password = it.take(128) }, Modifier.fillMaxWidth(), singleLine = true,
            enabled = !state.busy, visualTransformation = PasswordVisualTransformation(),
            label = { Text("Contraseña (vacía si entras con Google)") })
        MakeField("Escribe $DeleteWord para confirmar", confirm, { confirm = it.take(8) })
        MakeButton("Eliminar definitivamente", enabled = !state.busy && confirm.trim() == DeleteWord, icon = null) {
            model.deleteAccount(password, confirm)
        }
        MakeButton("Cancelar", ButtonKind.Ghost, enabled = !state.busy, icon = null) {
            open = false; password = ""; confirm = ""
        }
    }
}

/** Reportar o bloquear a un miembro desde la lista del grupo. */
@Composable
fun MemberSafety(member: Member, busy: Boolean, model: ConnectedViewModel) {
    var reporting by rememberSaveable(member.id) { mutableStateOf(false) }
    var blocking by rememberSaveable(member.id) { mutableStateOf(false) }
    var reason by rememberSaveable(member.id) { mutableStateOf(ReportReasons.first().first) }
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        TextButton(onClick = { reporting = !reporting; blocking = false }, enabled = !busy) { Text("Reportar") }
        TextButton(onClick = { blocking = !blocking; reporting = false }, enabled = !busy) { Text("Bloquear") }
    }
    if (reporting) {
        Segmented(ReportReasons.map { it.first }, reason) { reason = it }
        MakeButton("Enviar reporte de ${member.nickname}", ButtonKind.Secondary, enabled = !busy, icon = null) {
            reporting = false
            model.reportMember(member, ReportReasons.first { it.first == reason }.second)
        }
    }
    if (blocking) {
        Text("Si lo bloqueas, ya no podrán retarse entre ustedes.", style = MaterialTheme.typography.bodySmall)
        MakeButton("Bloquear a ${member.nickname}", ButtonKind.Secondary, enabled = !busy, icon = null) {
            blocking = false
            model.blockMember(member)
        }
    }
}
