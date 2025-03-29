package com.assignment.planets.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColorScheme = lightColorScheme(
    primary = primary_planet_dark,
    secondary = primary_planet,
    tertiary = Color.White,
    background = Color.White,
    surface = Color.Black
)

@Composable
fun PlanetsAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}