package com.assignment.planets.ui.feature.common.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.assignment.planets.ui.theme.primary_planet_dark
import com.assignment.planets.ui.theme.primary_planet

@Composable
fun LoadingView(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = modifier.width(64.dp),
            color = primary_planet_dark,
            trackColor = primary_planet,
        )
    }
}

@Preview
@Composable
fun PreviewLoadingView() {
    LoadingView()
}