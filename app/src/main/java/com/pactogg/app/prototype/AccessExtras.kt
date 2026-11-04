package com.pactogg.app.prototype

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun RecoveryPage(back: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }
    AppPage("Recupera tu acceso.", "Volvamos a encontrarnos") {
        Field("Correo electrónico", email) { email = it }
        Action("Revisar recuperación") { submitted = true }
        if (submitted) Text("No se envió un correo: la recuperación se conectará con el backend.")
        Action("Volver al acceso", onClick = back)
    }
}

@Composable
fun WelcomePage(done: () -> Unit) {
    var step by remember { mutableStateOf(0) }
    val titles = listOf("Prueba PactoGG", "Arma tu reto", "Acuerdos claros", "Demuestra tu palabra")
    val copies = listOf("Entre amigos. Sin dinero real.", "Elige juego, rival y condiciones.",
        "Revisa lo que aceptas antes de jugar.", "Comparte fotos o enlaces; la revisión llega después.")
    AppPage(titles[step], "Descubre cómo funciona un pacto") {
        PactCard(titles[step], copies[step])
        Action(if (step == 3) "Volver al acceso" else "Continuar") {
            if (step == 3) done() else step += 1
        }
        if (step > 0) Action("Anterior") { step -= 1 }
    }
}
