package com.equipoamazon.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val AmazonDark = Color(0xFF131921)
val AmazonOrange = Color(0xFFFF9900)
val SurfaceLight = Color(0xFFF4F4F4)
val SuccessGreen = Color(0xFF4CAF50)
val AmazonGrey = Color(0xFF555555)

private val LightColorScheme = lightColorScheme(
    primary = AmazonOrange,
    onPrimary = Color.White,
    secondary = AmazonDark,
    background = SurfaceLight,
    surface = Color.White,
    onSurface = AmazonDark
)

@Composable
fun AmazonTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
