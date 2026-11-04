package com.pactogg.app.prototype

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pactogg.app.ui.theme.PactoInput
import com.pactogg.app.R
import com.pactogg.app.core.designsystem.PageLayout
import com.pactogg.app.core.designsystem.AccentHeading

@Composable
fun AppPage(title: String, subtitle: String, content: @Composable ColumnScope.() -> Unit) {
    PageLayout {
        if (title.contains('\n')) AccentHeading(title)
        else Text(title, style = MaterialTheme.typography.headlineMedium)
        if (subtitle.isNotBlank()) Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant)
        content()
    }
}

@Composable
fun PactCard(title: String, copy: String, icon: Int = R.drawable.ic_pacto_shield,
    content: @Composable ColumnScope.() -> Unit = {}) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                IconBadge(icon)
                Text("PACTOGG", style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
            Text(copy, color = MaterialTheme.colorScheme.onSurfaceVariant)
            content()
        }
    }
}

@Composable
fun Action(label: String, enabled: Boolean = true, onClick: () -> Unit) {
    Button(onClick = onClick, enabled = enabled, shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth().heightIn(min = 50.dp)) {
        Text(label, modifier = Modifier.weight(1f))
        Icon(painterResource(R.drawable.ic_pacto_chevron), null, modifier = Modifier.size(18.dp))
    }
}

@Composable
fun Field(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(value, onChange, label = { Text(label) }, modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp), colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = PactoInput, unfocusedContainerColor = PactoInput,
            focusedBorderColor = MaterialTheme.colorScheme.secondary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            cursorColor = MaterialTheme.colorScheme.secondary))
}

@Composable
fun Choices(options: List<String>, selected: String, onSelect: (String) -> Unit) {
    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { option ->
            FilterChip(selected == option, onClick = { onSelect(option) }, label = { Text(option) },
                shape = RoundedCornerShape(7.dp), modifier = Modifier.heightIn(min = 44.dp),
                colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.secondary))
        }
    }
}
