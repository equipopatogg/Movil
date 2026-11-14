@file:Suppress("TooManyFunctions")

package com.pactogg.app.core.designsystem

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pactogg.app.R
import com.pactogg.app.ui.theme.PactoBackground
import com.pactogg.app.ui.theme.PactoCyan
import com.pactogg.app.ui.theme.PactoInput
import com.pactogg.app.ui.theme.PactoOrange
import com.pactogg.app.ui.theme.PactoRaised
import com.pactogg.app.ui.theme.PactoViolet

/*
 * Piezas visuales copiadas de la maqueta de Figma Make (src/index.css y refinements.css, spec UX-02).
 * Solo apariencia: ninguna pieza hace peticiones ni guarda estado de negocio.
 */

private val Ink = Color(0xFF050812)
private val CardColor = Color(0xFF11182B)
private val CardBorder = Color(0xFF263654)
private val Muted = Color(0xFF7F8AA5)
private val Body = Color(0xFFAEB7CC)
private val SafetyColor = Color(0xFF0C1424)

enum class Tone(val background: Color, val content: Color) {
    Orange(PactoOrange, PactoBackground), Cyan(PactoCyan, PactoBackground),
    Violet(PactoViolet, Color.White), Muted(Color(0xFF7F8AA5), PactoBackground),
}

enum class ButtonKind { Primary, Secondary, Ghost }

/** Página con encabezado de la maqueta (`page-header`): flecha de regreso, título y subtítulo. */
@Composable
fun MakePage(title: String, subtitle: String? = null, onBack: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit) {
    PageLayout {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()) {
            if (onBack != null) {
                IconButton(onClick = onBack, modifier = Modifier.size(42.dp)
                    .background(PactoRaised, RoundedCornerShape(10.dp))
                    .border(1.dp, CardBorder, RoundedCornerShape(10.dp))) {
                    Icon(painterResource(R.drawable.ic_pacto_chevron), "Volver",
                        modifier = Modifier.size(18.dp).rotate(180f))
                }
            }
            Column(Modifier.weight(1f)) {
                Text(title.uppercase(), style = MaterialTheme.typography.headlineSmall)
                if (!subtitle.isNullOrBlank()) Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Body)
            }
        }
        content()
    }
}

/** Letrero chico en mayúsculas (`small` de la maqueta). */
@Composable
fun Kicker(text: String, color: Color = Muted) {
    Text(text.uppercase(), color = color, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold,
        letterSpacing = 1.2.sp)
}

/** Título de sección con letrero (`section-title`). */
@Composable
fun SectionTitle(kicker: String, title: String, trailing: @Composable RowScope.() -> Unit = {}) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Kicker(kicker)
            Text(title, style = MaterialTheme.typography.titleLarge)
        }
        trailing()
    }
}

/** Título grande con la segunda línea de color (`Juega con ganas. / Cumple tu palabra.`). */
@Composable
fun HeroTitle(kicker: String, title: String, copy: String? = null) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Kicker(kicker)
        AccentHeading(title)
        if (copy != null) Text(copy, color = Body, style = MaterialTheme.typography.bodyLarge)
    }
}

/** Tarjeta de la maqueta (`card`). Si tiene onClick, toda la tarjeta es tocable. */
@Composable
fun MakeCard(modifier: Modifier = Modifier, accent: Color? = null, onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit) {
    val shape = RoundedCornerShape(12.dp)
    Surface(shape = shape, color = CardColor, border = BorderStroke(1.dp, accent ?: CardBorder),
        modifier = modifier.fillMaxWidth().let { if (onClick != null) it.clickable(role = Role.Button,
            onClick = onClick) else it }) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp), content = content)
    }
}

/** Etiqueta de estado (`pill`). */
@Composable
fun Pill(text: String, tone: Tone = Tone.Violet) {
    Box(Modifier.background(tone.background, RoundedCornerShape(6.dp)).border(1.dp, Ink, RoundedCornerShape(6.dp))
        .padding(horizontal = 8.dp, vertical = 4.dp)) {
        Text(text.uppercase(), color = tone.content, fontSize = 9.sp, fontWeight = FontWeight.Black,
            letterSpacing = 0.8.sp)
    }
}

/** Insignia cuadrada con las iniciales del juego (`game-mark`). */
@Composable
fun GameMark(game: String, tone: Tone = Tone.Cyan) {
    val initials = game.split(' ', '-', ':').filter { it.isNotBlank() }.take(2)
        .joinToString("") { it.first().uppercase() }.ifEmpty { "GG" }
    Box(Modifier.size(44.dp).background(tone.background, RoundedCornerShape(10.dp))
        .border(2.dp, Ink, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
        Text(initials, color = tone.content, style = MaterialTheme.typography.titleSmall)
    }
}

/** Círculo con la inicial del apodo (`avatar`). */
@Composable
fun Initial(name: String, tone: Tone = Tone.Violet, size: Int = 30) {
    Box(Modifier.size(size.dp).background(tone.background, CircleShape).border(2.dp, Ink, CircleShape),
        contentAlignment = Alignment.Center) {
        Text(name.take(1).uppercase().ifEmpty { "?" }, color = tone.content, fontSize = 11.sp,
            fontWeight = FontWeight.Black)
    }
}

/** Botón de la maqueta (`btn-primary`, `btn-secondary`, `btn-ghost`). */
@Composable
fun MakeButton(label: String, kind: ButtonKind = ButtonKind.Primary, enabled: Boolean = true,
    @DrawableRes icon: Int? = R.drawable.ic_pacto_chevron, onClick: () -> Unit) {
    val (container, content, border) = when (kind) {
        ButtonKind.Primary -> Triple(PactoOrange, PactoBackground, Ink)
        ButtonKind.Secondary -> Triple(Color.Transparent, PactoCyan, PactoCyan)
        ButtonKind.Ghost -> Triple(PactoRaised, Color(0xFFD9DEEA), CardBorder)
    }
    Button(onClick = onClick, enabled = enabled, shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, border),
        colors = ButtonDefaults.buttonColors(containerColor = container, contentColor = content,
            disabledContainerColor = container.copy(alpha = 0.4f), disabledContentColor = content.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
        Text(label, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f))
        if (icon != null) Icon(painterResource(icon), null, modifier = Modifier.size(18.dp))
    }
}

/** Aviso con escudo (`safety`). */
@Composable
fun SafetyNote(text: String, @DrawableRes icon: Int = R.drawable.ic_pacto_shield) {
    Row(Modifier.fillMaxWidth().background(SafetyColor, RoundedCornerShape(10.dp))
        .border(1.dp, CardBorder, RoundedCornerShape(10.dp)).padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(painterResource(icon), null, tint = PactoCyan, modifier = Modifier.size(18.dp))
        Text(text, color = Body, style = MaterialTheme.typography.bodySmall)
    }
}

/** Pestañas en una barra (`segmented` y `home-tabs`). */
@Composable
fun Segmented(options: List<String>, selected: String, onSelect: (String) -> Unit) {
    Row(Modifier.fillMaxWidth().background(CardColor, RoundedCornerShape(12.dp))
        .border(1.dp, CardBorder, RoundedCornerShape(12.dp)).padding(4.dp)
        .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        options.forEach { option ->
            val active = option == selected
            Box(Modifier.heightIn(min = 40.dp)
                .background(if (active) PactoCyan else Color.Transparent, RoundedCornerShape(8.dp))
                .clickable(role = Role.Tab) { onSelect(option) }.padding(horizontal = 14.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center) {
                Text(option, color = if (active) PactoBackground else Muted, fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

/** Caja punteada para cuando no hay datos (`empty-state`). */
@Composable
fun EmptyState(title: String, copy: String, @DrawableRes icon: Int = R.drawable.ic_pacto_search) {
    Column(Modifier.fillMaxWidth().border(1.dp, Color(0xFF3B4563), RoundedCornerShape(14.dp))
        .background(CardColor, RoundedCornerShape(14.dp)).padding(horizontal = 22.dp, vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Icon(painterResource(icon), null, tint = PactoCyan, modifier = Modifier.size(30.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
        Text(copy, color = Body, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
    }
}

/** Renglón de navegación con ícono, título y descripción (`profile-link`). */
@Composable
fun LinkCard(title: String, copy: String, @DrawableRes icon: Int, badge: String? = null, onClick: () -> Unit) {
    MakeCard(onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(40.dp).background(PactoRaised, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center) {
                Icon(painterResource(icon), null, tint = PactoCyan, modifier = Modifier.size(20.dp))
            }
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall)
                Text(copy, color = Body, style = MaterialTheme.typography.bodySmall)
            }
            if (badge != null) Pill(badge, Tone.Orange)
            Icon(painterResource(R.drawable.ic_pacto_chevron), null, tint = Muted, modifier = Modifier.size(16.dp))
        }
    }
}

/** Dato con etiqueta (`summary dl`). */
@Composable
fun InfoRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Muted, style = MaterialTheme.typography.bodySmall)
        Text(value, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.End,
            modifier = Modifier.padding(start = 12.dp))
    }
}

/** Campo de la maqueta: etiqueta en mayúsculas arriba y caja oscura. */
@Composable
fun MakeField(label: String, value: String, onChange: (String) -> Unit, error: String? = null,
    hint: String? = null, multiline: Boolean = false, number: Boolean = false) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Kicker(label, Color(0xFF8F9BB5))
        OutlinedTextField(value, onChange, modifier = Modifier.fillMaxWidth(), singleLine = !multiline,
            minLines = if (multiline) 3 else 1, isError = error != null,
            keyboardOptions = if (number) KeyboardOptions(keyboardType = KeyboardType.Number)
                else KeyboardOptions.Default,
            supportingText = (error ?: hint)?.let { { Text(it) } },
            shape = RoundedCornerShape(10.dp), colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = PactoInput, unfocusedContainerColor = PactoInput,
                focusedBorderColor = PactoCyan, unfocusedBorderColor = CardBorder, cursorColor = PactoCyan))
    }
}

/** Texto en español para los estados que manda el servidor. */
fun statusLabel(status: String): String = when (status) {
    "open" -> "Abierto"
    "in_progress" -> "En curso"
    "voting" -> "En votación"
    "fulfilled" -> "Cumplido"
    "cheating" -> "Hizo trampa"
    "expired" -> "Vencido"
    "cancelled" -> "Cancelado"
    "rejected" -> "Rechazado"
    "pending" -> "Pendiente"
    "reported" -> "Reportada"
    "disputed" -> "En disputa"
    "confirmed" -> "Confirmada"
    "finished" -> "Terminado"
    "completed" -> "Cumplido"
    "pending_confirmation" -> "Por confirmar"
    "admin" -> "Administrador"
    "member" -> "Miembro"
    else -> status
}

/** Color de etiqueta según el estado. */
fun statusTone(status: String): Tone = when (status) {
    "open", "pending", "reported" -> Tone.Orange
    "in_progress", "voting", "disputed" -> Tone.Cyan
    "fulfilled", "confirmed", "finished", "completed" -> Tone.Violet
    else -> Tone.Muted
}

/** Fecha ISO del servidor a "AAAA-MM-DD HH:mm" en la hora del teléfono; si no se puede, la deja igual. */
fun localDate(iso: String?): String {
    if (iso.isNullOrBlank()) return "—"
    val clean = iso.substringBefore('.').removeSuffix("Z")
    return runCatching {
        val input = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.ROOT).apply {
            timeZone = java.util.TimeZone.getTimeZone("UTC"); isLenient = false
        }
        val date = requireNotNull(input.parse(clean))
        java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.ROOT).format(date)
    }.getOrDefault(iso)
}
