package com.pactogg.app.feature.connected.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.pactogg.app.R
import com.pactogg.app.prototype.Action
import com.pactogg.app.prototype.AppPage
import com.pactogg.app.prototype.Field
import com.pactogg.app.prototype.PactCard

@Composable
fun ConnectedProfile(state: ConnectedState, model: ConnectedViewModel) {
    AppPage("Tu perfil.", "Tu palabra también cuenta") {
        val profile = state.social.profile
        PactCard(state.account?.nickname.orEmpty(), state.account?.email.orEmpty(), R.drawable.ic_pacto_user) {
            Action("Actualizar perfil", enabled = !state.busy) { model.loadProfile() }
        }
        if (profile != null) {
            PactCard("Tu trayectoria", "Estadísticas recibidas del servidor", R.drawable.ic_pacto_trophy) {
                Text("Índice de palabra: ${profile.wordIndex}")
                Text("Completados: ${profile.completed} · Incumplidos: ${profile.cheated}")
                Text("Racha: ${profile.streak} · Trofeos: ${profile.trophies}")
                Text("Grupos: ${profile.groupsCount}")
            }
            PactCard("Editar apodo", "De 3 a 24 caracteres: letras, números, punto, guion o guion bajo.") {
                Field("Nuevo apodo", state.social.editedNickname, model::editNickname)
                state.social.nicknameNotice?.let { Text(it) }
                Action("Guardar apodo", enabled = !state.busy &&
                    state.social.editedNickname.trim() != profile.account.nickname) { model.saveProfile() }
                Text("El selector de avatares estará disponible cuando se confirme el catálogo.")
            }
        }
        Text("El cierre de sesión borra la sesión local; no elimina tu cuenta.")
        Action("Cerrar sesión", enabled = !state.busy) { model.logout() }
    }
}
