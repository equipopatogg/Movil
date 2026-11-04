package com.pactogg.app.feature.auth.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import com.pactogg.app.R

/** Decorative illustration recreated from Make CSS; no image loading or network. */
@Composable
fun AccessArtwork(registering: Boolean) {
    val outline = MaterialTheme.colorScheme.outline
    Box(Modifier.fillMaxWidth().height(164.dp).clearAndSetSemantics { }, contentAlignment = Alignment.Center) {
        Canvas(Modifier.size(256.dp, 144.dp)) {
            rotate(-15f) {
                drawOval(outline, Offset(size.width * .08f, size.height * .2f),
                    Size(size.width * .84f, size.height * .56f), style = Stroke(1.dp.toPx()))
            }
            rotate(20f) {
                drawOval(outline.copy(alpha = .65f), Offset(size.width * .19f, size.height * .08f),
                    Size(size.width * .62f, size.height * .78f), style = Stroke(1.dp.toPx()))
            }
        }
        Surface(
            modifier = Modifier.offset(y = (-10).dp).rotate(-8f).size(84.dp, 98.dp),
            shape = RoundedCornerShape(17.dp), color = MaterialTheme.colorScheme.tertiaryContainer,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)) {
                Icon(painterResource(if (registering) R.drawable.ic_pacto_users else R.drawable.ic_pacto_bolt),
                    contentDescription = null, tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(38.dp))
                Text(stringResource(if (registering) R.string.ui_art_squad else R.string.ui_art_pact),
                    style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
            }
        }
        Surface(modifier = Modifier.offset(x = 48.dp, y = (-48).dp).size(30.dp),
            shape = RoundedCornerShape(9.dp), color = MaterialTheme.colorScheme.secondary,
            border = BorderStroke(3.dp, MaterialTheme.colorScheme.background)) {
            Icon(painterResource(R.drawable.ic_pacto_check), contentDescription = null,
                tint = MaterialTheme.colorScheme.background)
        }
        Text(stringResource(R.string.ui_art_caption), modifier = Modifier.align(Alignment.BottomCenter),
            style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
