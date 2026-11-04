package com.pactogg.app.prototype

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pactogg.app.R
import com.pactogg.app.core.designsystem.PageLayout
import com.pactogg.app.core.designsystem.AccentHeading

@Composable
fun HomeDesign(nav: (String) -> Unit) {
    var filter by remember { mutableStateOf("Todos") }
    var help by remember { mutableStateOf(false) }
    PageLayout {
        GroupSwitch { nav("groups") }
        Text("BIENVENIDO A TU TABLERO", style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        AccentHeading("Juega con ganas.\nCumple tu palabra.")
        Text("Todo lo que acuerdas con tu grupo, en un mismo lugar.",
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        HomeShortcuts(nav)
        Text("CADA PACTO, UN SIGUIENTE PASO", style = MaterialTheme.typography.labelSmall)
        Text("Lo que sigue", style = MaterialTheme.typography.titleLarge)
        Choices(listOf("Todos", "Tu turno", "Jurado"), filter) { filter = it }
        if (filter != "Jurado") PactCard("Gana 3 partidas seguidas", "ROCKET LEAGUE · Con Martín · Ejemplo",
            R.drawable.ic_pacto_bolt) { Action("Ver contrato y evidencia") { nav("contract") } }
        if (filter != "Tu turno") PactCard("Una revisión, tu criterio.", "Reto de Lucía · Práctica en equipo",
            R.drawable.ic_pacto_vote) { Action("Revisar el pacto") { nav("jury") } }
        HeroPanel("EL RETO SE JUEGA EN EQUIPO", "Hay lugar para tu grupo.",
            "Explora los torneos amistosos", R.drawable.ic_pacto_trophy)
        Action("Ver torneos") { nav("tournaments") }
        TextButton(onClick = { help = !help }) { Text("¿Cómo funciona un pacto?") }
        if (help) Text("Acuerden las condiciones. Juega y comparte fotos o enlaces. La revisión llega después.")
    }
}

@Composable
fun GroupSwitch(onClick: () -> Unit) {
    Surface(shape = RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically) {
            IconBadge(R.drawable.ic_pacto_users)
            Column(Modifier.weight(1f)) {
                Text("TU ESPACIO · DATOS LOCALES", style = MaterialTheme.typography.labelSmall)
                Text("Squad de los jueves", style = MaterialTheme.typography.titleSmall)
            }
            Icon(painterResource(R.drawable.ic_pacto_chevron), null)
        }
    }
}

@Composable
private fun HomeShortcuts(nav: (String) -> Unit) {
    val titles = listOf("Con tu voz", "Tu grupo", "Ranking")
    val captions = listOf("Habla y edita", "Juega en grupo", "Compara tu palabra")
    val routes = listOf("voice", "groups", "ranking")
    val icons = listOf(R.drawable.ic_pacto_mic, R.drawable.ic_pacto_users, R.drawable.ic_pacto_trophy)
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        titles.forEachIndexed { index, title ->
            Surface(shape = RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.weight(1f).clickable { nav(routes[index]) }) {
                Column(Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(painterResource(icons[index]), null, Modifier.size(23.dp),
                        tint = if (index == 0) MaterialTheme.colorScheme.tertiary
                            else MaterialTheme.colorScheme.secondary)
                    Text(title, style = MaterialTheme.typography.labelLarge)
                    Text(captions[index], style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
