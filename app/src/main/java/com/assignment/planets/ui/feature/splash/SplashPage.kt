package com.assignment.planets.ui.feature.splash

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.assignment.planets.R
import com.assignment.planets.ui.theme.primary_planet

@Composable
fun SplashScreenPage(navigateToDestination: () -> Unit) {
    LaunchedEffect(Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToDestination.invoke()
        }, 2000)
    }
    SplashScreenPageContent()
}

@Composable
fun SplashScreenPageContent(modifier: Modifier = Modifier) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = primary_planet),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.icon_planets),
            contentDescription = null,
            modifier = Modifier.size(240.dp)
        )
    }
}

@Preview
@Composable
fun PreviewSplashScreenPageContent() {
    SplashScreenPageContent()
}