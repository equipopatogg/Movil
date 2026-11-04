package com.pactogg.app.core.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@Composable
fun AccentHeading(text: String, centered: Boolean = false) {
    val parts = text.split('\n', limit = 2)
    Text(buildAnnotatedString {
        append(parts.first())
        if (parts.size == 2) {
            append('\n')
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.tertiary)) { append(parts.last()) }
        }
    }, style = MaterialTheme.typography.headlineLarge, modifier = Modifier.fillMaxWidth(),
        textAlign = if (centered) TextAlign.Center else TextAlign.Start)
}
