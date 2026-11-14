package com.pactogg.app.feature.connected.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pactogg.app.R
import com.pactogg.app.core.designsystem.ButtonKind
import com.pactogg.app.core.designsystem.Initial
import com.pactogg.app.core.designsystem.Kicker
import com.pactogg.app.core.designsystem.LinkCard
import com.pactogg.app.core.designsystem.MakeButton
import com.pactogg.app.core.designsystem.MakeCard
import com.pactogg.app.core.designsystem.MakeField
import com.pactogg.app.core.designsystem.MakePage
import com.pactogg.app.core.designsystem.SectionTitle
import com.pactogg.app.core.designsystem.Tone
import com.pactogg.app.feature.connected.domain.Profile
import com.pactogg.app.ui.theme.PactoCyan

/** Perfil según la maqueta (`Profile`): identidad, palabra, accesos, privacidad y cierre de sesión. */
@Composable
fun ConnectedProfile(state: ConnectedState, model: ConnectedViewModel) {
    var confirmLogout by rememberSaveable { mutableStateOf(false) }
    var privacy by rememberSaveable { mutableStateOf(false) }
    if (confirmLogout) ActionConfirmation("¿Salir de PactoGG?",
        "Tendrás que iniciar sesión de nuevo para entrar a tu cuenta. Tu cuenta no se eliminará.",
        "Cerrar sesión", !state.busy,
        onConfirm = { confirmLogout = false; model.logout() }, onDismiss = { confirmLogout = false })
    MakePage("Tu perfil", "Tu identidad dentro del squad", onBack = { model.back() }) {
        MakeCard {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Initial(state.account?.nickname.orEmpty(), Tone.Orange, 56)
                Column(Modifier.weight(1f)) {
                    Kicker("Tu identidad gamer", PactoCyan)
                    Text(state.account?.nickname.orEmpty(), style = MaterialTheme.typography.titleLarge)
                    Text(state.account?.email.orEmpty(), style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            MakeButton("Actualizar perfil", ButtonKind.Ghost, enabled = !state.busy) { model.loadProfile() }
        }
        state.social.profile?.let { profile ->
            WordCard(profile)
            MakeCard {
                Kicker("Editar apodo")
                MakeField("Nuevo apodo", state.social.editedNickname, model::editNickname,
                    error = state.inlineFeedback(), hint = "De 3 a 24: letras, números, punto, guion o guion bajo.")
                state.social.nicknameNotice?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
                MakeButton("Guardar apodo", enabled = !state.busy &&
                    state.social.editedNickname.trim() != profile.account.nickname) { model.saveProfile() }
            }
        }
        SectionTitle("Tu espacio", "Dentro de PactoGG")
        LinkCard("Mis grupos", "Tu squad y sus invitaciones", R.drawable.ic_pacto_users) {
            model.navigate(ConnectedRoute.GROUPS)
        }
        LinkCard("Privacidad y seguridad", "Cómo se protege tu palabra", R.drawable.ic_pacto_shield) {
            privacy = !privacy
        }
        if (privacy) MakeCard {
            Text("Los grupos son privados: solo sus miembros pueden consultar sus contratos.",
                style = MaterialTheme.typography.bodySmall)
            Text("El historial de acciones es de solo agregar: los resultados cerrados no se editan.",
                style = MaterialTheme.typography.bodySmall)
            Text("Cerrar sesión borra la sesión de este teléfono; no elimina tu cuenta.",
                style = MaterialTheme.typography.bodySmall)
        }
        MakeButton("Cerrar sesión", ButtonKind.Ghost, enabled = !state.busy) { confirmLogout = true }
    }
}

@Composable
private fun WordCard(profile: Profile) {
    MakeCard {
        Kicker("Índice de palabra")
        Text("Tu palabra tiene valor.", style = MaterialTheme.typography.titleMedium)
        Text("${profile.wordIndex}", style = MaterialTheme.typography.headlineLarge, color = PactoCyan)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Stat("Cumplidos", profile.completed, Modifier.weight(1f))
            Stat("Trampas", profile.cheated, Modifier.weight(1f))
            Stat("Racha", profile.streak, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Stat("Trofeos", profile.trophies, Modifier.weight(1f))
            Stat("Grupos", profile.groupsCount, Modifier.weight(1f))
        }
    }
}

@Composable
private fun Stat(label: String, value: Int, modifier: Modifier) {
    Column(modifier) {
        Text(value.toString(), style = MaterialTheme.typography.titleLarge)
        Kicker(label)
    }
}
