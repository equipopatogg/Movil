package com.pactogg.app.ui.theme

import android.os.Build
import androidx.annotation.FontRes
import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.pactogg.app.R

@OptIn(ExperimentalTextApi::class)
private fun makeFamily(@FontRes resource: Int, weights: List<Int>): FontFamily =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        FontFamily(weights.map { weight ->
            Font(resource, weight = FontWeight(weight),
                variationSettings = FontVariation.Settings(FontVariation.weight(weight)))
        })
    } else FontFamily.SansSerif

private val BodyFont = makeFamily(R.font.inter_variable, listOf(400, 500, 600, 700))
private val HeadingFont = makeFamily(R.font.spacegrotesk_variable, listOf(600, 700))

private fun heading(size: Int, height: Int, spacing: Double = -0.5) = TextStyle(
    fontFamily = HeadingFont, fontWeight = FontWeight.Bold,
    fontSize = size.sp, lineHeight = height.sp, letterSpacing = spacing.sp,
)

private fun body(size: Int, height: Int) = TextStyle(
    fontFamily = BodyFont, fontWeight = FontWeight.Normal,
    fontSize = size.sp, lineHeight = height.sp, letterSpacing = 0.sp,
)

val Typography = Typography(
    headlineLarge = heading(32, 36, -1.0),
    headlineMedium = heading(25, 29),
    headlineSmall = heading(22, 27),
    titleLarge = heading(21, 25),
    titleMedium = heading(17, 22, 0.0),
    titleSmall = heading(14, 20, 0.0),
    bodyLarge = body(14, 23),
    bodyMedium = body(13, 21),
    bodySmall = body(11, 17),
    labelLarge = heading(13, 18, 0.0),
    labelMedium = heading(10, 15, 0.7),
    labelSmall = heading(9, 14, 0.8),
)
