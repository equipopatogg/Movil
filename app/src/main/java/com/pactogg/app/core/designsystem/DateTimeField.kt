package com.pactogg.app.core.designsystem

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.pactogg.app.R
import com.pactogg.app.ui.theme.PactoCyan
import com.pactogg.app.ui.theme.PactoInput
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val Pattern = "yyyy-MM-dd HH:mm"

/**
 * Fecha y hora con el calendario y el reloj del sistema, en vez de escribirlas a mano.
 * El valor sigue siendo "AAAA-MM-DD HH:mm" en la hora del teléfono; solo se pueden elegir fechas futuras.
 */
@Composable
fun DateTimeField(label: String, value: String, enabled: Boolean = true, hint: String? = null,
    onChange: (String) -> Unit) {
    val context = LocalContext.current
    val open = {
        val start = Calendar.getInstance().apply {
            parse(value)?.let { time = it } ?: add(Calendar.DAY_OF_MONTH, 1)
        }
        DatePickerDialog(context, { _, year, month, day ->
            TimePickerDialog(context, { _, hour, minute ->
                val chosen = Calendar.getInstance().apply { set(year, month, day, hour, minute, 0) }
                onChange(SimpleDateFormat(Pattern, Locale.ROOT).format(chosen.time))
            }, start.get(Calendar.HOUR_OF_DAY), start.get(Calendar.MINUTE), true).show()
        }, start.get(Calendar.YEAR), start.get(Calendar.MONTH), start.get(Calendar.DAY_OF_MONTH)).apply {
            datePicker.minDate = System.currentTimeMillis() - 1000
        }.show()
    }
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Kicker(label, Color(0xFF8F9BB5))
        Surface(shape = RoundedCornerShape(10.dp), color = PactoInput,
            border = BorderStroke(1.dp, Color(0xFF263654)),
            modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)
                .clickable(enabled = enabled, role = Role.Button, onClick = open)) {
            Row(Modifier.padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(value.ifBlank { "Elegir fecha y hora" }, modifier = Modifier.weight(1f),
                    color = if (value.isBlank()) MaterialTheme.colorScheme.onSurfaceVariant
                        else MaterialTheme.colorScheme.onSurface)
                Icon(painterResource(R.drawable.ic_pacto_chevron), null, tint = PactoCyan,
                    modifier = Modifier.size(18.dp))
            }
        }
        hint?.let {
            Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

private fun parse(value: String) = runCatching {
    SimpleDateFormat(Pattern, Locale.ROOT).apply { isLenient = false }.parse(value)
}.getOrNull()
