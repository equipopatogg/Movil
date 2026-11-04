package com.pactogg.app.prototype

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pactogg.app.R

@Composable
fun TournamentFeature(onOpen: () -> Unit) {
    Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            TournamentArtwork(R.drawable.ic_pacto_trophy)
            Text("INSCRIPCIÓN ABIERTA · DATOS LOCALES", style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary)
            Text("ROCKET LEAGUE · 2 VS 2", style = MaterialTheme.typography.labelSmall)
            Text("Copa del finde", style = MaterialTheme.typography.headlineMedium)
            Text("Tu grupo, un cuadro y un pacto: competir con reglas claras.",
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Equipos de 2 · Eliminación directa", style = MaterialTheme.typography.bodySmall)
            Action("Ver torneo", onClick = onOpen)
        }
    }
}
