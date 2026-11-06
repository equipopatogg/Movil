package com.pactogg.app.feature.connected.presentation

import androidx.compose.ui.res.stringResource
import com.pactogg.app.R
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import com.pactogg.app.feature.connected.domain.EvidenceRules
import com.pactogg.app.feature.connected.domain.InputRules
import com.pactogg.app.prototype.Action
import com.pactogg.app.prototype.PactCard

@Composable
internal fun CommunityFame(state: ConnectedState) {
    val handler = LocalUriHandler.current
    if (state.community.loaded && state.community.fame.isEmpty()) Text(stringResource(
        R.string.community_todavia_no_hay_retos_en_el_muro))
    state.community.fame.forEach { item ->
        PactCard(item.executor?.nickname ?: "Reto cumplido", item.game) {
            Text(item.condition)
            Text(item.reward)
            item.resolvedAt?.let { Text("Cumplido: $it") }
            item.photos.forEach { url ->
                val safe = EvidenceRules.imageUrl(url)
                if (safe != null) EvidenceImage(safe, "Evidencia del reto cumplido")
                else Text(stringResource(R.string.community_imagen_no_disponible))
            }
            item.links.filter(InputRules::evidenceLink).forEachIndexed { index, url ->
                Action("Abrir enlace ${index + 1}", enabled = !state.busy) {
                    runCatching { handler.openUri(url) }
                }
            }
        }
    }
}
