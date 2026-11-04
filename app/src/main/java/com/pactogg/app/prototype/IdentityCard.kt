package com.pactogg.app.prototype

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IdentityCard(name: String, handle: String, avatar: String, onEdit: () -> Unit) {
    Surface(shape = RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)) {
        Column {
            Surface(color = MaterialTheme.colorScheme.tertiaryContainer) {
                Box(Modifier.fillMaxWidth().height(100.dp)) {
                    Text("PG", modifier = Modifier.align(Alignment.CenterEnd).rotate(-10f),
                        fontSize = 94.sp, color = MaterialTheme.colorScheme.tertiary.copy(alpha = .16f))
                    Text("TU IDENTIDAD, TU PALABRA", Modifier.padding(18.dp),
                        style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
                }
            }
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    val tint = when (avatar) {
                        "Cian" -> MaterialTheme.colorScheme.secondary
                        "Naranja" -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.tertiary
                    }
                    Surface(Modifier.size(64.dp), shape = RoundedCornerShape(16.dp), color = tint) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(name.take(2).uppercase(), style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.background)
                        }
                    }
                    Column {
                        Text("GAMERTAG", style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary)
                        Text(name, style = MaterialTheme.typography.titleLarge)
                        Text("@$handle", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                SecondaryAction("Editar identidad", onEdit)
            }
        }
    }
}
