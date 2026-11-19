package com.pactogg.app.core.designsystem

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pactogg.app.feature.connected.domain.EvidenceRules

/** Avatar de una persona (USR-07): su foto si la subió; si no, o si no carga, el círculo de color con su inicial. */
@Composable
fun AvatarBadge(name: String, avatarId: String, url: String?, size: Int = 30) {
    val photo = url?.takeIf { avatarId == "photo" }?.let(EvidenceRules::imageUrl)
    var failed by remember(photo) { mutableStateOf(false) }
    if (photo == null || failed) {
        Initial(name, avatarTone(avatarId), size)
        return
    }
    AsyncImage(model = photo, contentDescription = "Foto de perfil de $name", contentScale = ContentScale.Crop,
        onError = { failed = true },
        modifier = Modifier.size(size.dp).clip(CircleShape).border(2.dp, Color(0xFF050812), CircleShape))
}
