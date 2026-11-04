package com.pactogg.app.prototype

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp

@Composable
fun IconBadge(icon: Int, lavender: Boolean = false) {
    Surface(shape = RoundedCornerShape(10.dp),
        color = if (lavender) MaterialTheme.colorScheme.tertiaryContainer
            else MaterialTheme.colorScheme.secondaryContainer,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)) {
        Icon(painterResource(icon), null, modifier = Modifier.padding(10.dp).size(23.dp),
            tint = if (lavender) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary)
    }
}

@Composable
fun HeroPanel(kicker: String, title: String, copy: String, icon: Int) {
    Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(13.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(18.dp), horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(kicker, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                Text(title, style = MaterialTheme.typography.headlineMedium)
                Text(copy, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Box(Modifier.rotate(12f)) { IconBadge(icon, lavender = true) }
        }
    }
}

@Composable
fun TournamentArtwork(icon: Int) {
    val line = MaterialTheme.colorScheme.outline
    Box(Modifier.fillMaxWidth().height(118.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(MaterialTheme.colorScheme.surfaceContainerLow).clearAndSetSemantics { },
        contentAlignment = Alignment.Center) {
        Canvas(Modifier.size(150.dp)) {
            for (factor in listOf(.7f, 1f)) {
                val edge = size.width * factor
                rotate(45f) {
                    drawRect(line, topLeft = androidx.compose.ui.geometry.Offset(
                        (size.width - edge) / 2, (size.height - edge) / 2),
                        size = androidx.compose.ui.geometry.Size(edge, edge), style = Stroke(1.dp.toPx()))
                }
            }
        }
        Icon(painterResource(icon), null, modifier = Modifier.size(58.dp), tint = MaterialTheme.colorScheme.secondary)
    }
}

@Composable
fun EmptyPodium() {
    Row(Modifier.fillMaxWidth().height(170.dp).clearAndSetSemantics { },
        horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Bottom) {
        listOf(2 to 78, 1 to 128, 3 to 62).forEach { (place, height) ->
            Surface(Modifier.weight(1f).height(height.dp), shape = RoundedCornerShape(9.dp, 9.dp, 0.dp, 0.dp),
                color = if (place == 1) MaterialTheme.colorScheme.tertiaryContainer
                    else MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                    Text(place.toString(), style = MaterialTheme.typography.titleLarge)
                    Text("—", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
