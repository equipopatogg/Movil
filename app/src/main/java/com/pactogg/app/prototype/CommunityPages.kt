package com.pactogg.app.prototype

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.pactogg.app.R

@Composable
fun GroupsPage() {
    var tab by remember { mutableStateOf("Mis grupos") }
    var groups by remember { mutableStateOf(listOf("Squad de los jueves")) }
    var input by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    AppPage("Grupos", "Un espacio privado. Tu gente. Tus pactos.") {
        HeroPanel("JUEGA CON LOS TUYOS", "Encuentra tu grupo.",
            "Acuerdos claros empiezan por un buen equipo.", R.drawable.ic_pacto_users)
        Choices(listOf("Mis grupos", "Unirme", "Crear grupo"), tab) { tab = it; message = "" }
        if (tab == "Mis grupos") groups.forEach { group ->
            PactCard(group, "PRIVADO · Grupo local de ejemplo", R.drawable.ic_pacto_users) {
                TextButton(onClick = { message = "Código de muestra PACTO-DEMO. No envía invitaciones reales." }) {
                    Text("Invitar")
                }
            }
        } else {
            Field(if (tab == "Unirme") "Código de invitación" else "Nombre del grupo", input) { input = it }
            Action(if (tab == "Unirme") "Buscar grupo" else "Crear mi grupo") {
                if (input.isNotBlank() && tab == "Crear grupo") {
                    groups = groups + input.trim(); input = ""; tab = "Mis grupos"
                    message = "Grupo añadido solo para esta vista local."
                } else message = "No se consultó un grupo real. Puedes crear uno local para probar."
            }
        }
        if (message.isNotEmpty()) Text(message, color = MaterialTheme.colorScheme.secondary)
    }
}

@Composable
fun ProfilePage(nav: (String) -> Unit) {
    var name by remember { mutableStateOf("Alex Gamer") }
    var handle by remember { mutableStateOf("alexgg") }
    var editing by remember { mutableStateOf(false) }
    var color by remember { mutableStateOf("Violeta") }
    var privacy by remember { mutableStateOf(false) }
    var logout by remember { mutableStateOf(false) }
    AppPage("Tu perfil", "Tu identidad dentro del grupo · datos de muestra") {
        IdentityCard(name, handle, color) { editing = !editing }
        if (editing) {
            Field("Apodo", name) { name = it }
            Field("Gamertag", handle) { handle = it }
            Choices(listOf("Violeta", "Cian", "Naranja"), color) { color = it }
            Action("Guardar cambios locales") { editing = false }
        }
        PactCard("Tu palabra tiene valor.", "Sin compras, canjes ni dinero real.", R.drawable.ic_pacto_bolt)
        LinkRow("Mis grupos", "Tu grupo y sus invitaciones", R.drawable.ic_pacto_users) { nav("groups") }
        LinkRow("Ranking y compromisos", "Palabra, fama e integridad", R.drawable.ic_pacto_trophy) { nav("ranking") }
        LinkRow("Notificaciones", "Avisos de tus pactos", R.drawable.ic_pacto_bell) { nav("notifications") }
        TextButton(onClick = { privacy = !privacy }) { Text("Privacidad y seguridad") }
        if (privacy) Text("Grupos privados. El aviso legal y eliminar cuenta requieren integración real.")
        TextButton(onClick = { logout = true }) { Text("Cerrar sesión") }
    }
    if (logout) AlertDialog(onDismissRequest = { logout = false }, title = { Text("¿Salir de PactoGG?") },
        text = { Text("Volverás al acceso. No hay una sesión de servidor que cerrar.") },
        confirmButton = { TextButton(onClick = { nav("access") }) { Text("Salir") } },
        dismissButton = { TextButton(onClick = { logout = false }) { Text("Seguir aquí") } })
}
