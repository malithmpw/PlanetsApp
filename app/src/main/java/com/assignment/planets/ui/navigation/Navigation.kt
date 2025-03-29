package com.assignment.planets.ui.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.assignment.planets.ui.feature.planetDetails.PlanetDetailsPage
import com.assignment.planets.ui.feature.planets.PlanetsPage
import com.assignment.planets.ui.feature.splash.SplashScreenPage

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = SplashScreen
    ) {
        composable<SplashScreen> {
            SplashScreenPage {
                navController.navigate(PlanetsScreen) {
                    popUpTo(SplashScreen) { inclusive = true }
                }
            }
        }

        composable<PlanetsScreen> {
            val activity = LocalContext.current as? Activity
            PlanetsPage(
                onBackPressed = {
                    activity?.finish()
                }
            ) { planetId ->
                navController.navigate(PlanetDetailsScreen(planetId = planetId))
            }
        }
        composable<PlanetDetailsScreen> {
            val planetId = it.toRoute<PlanetDetailsScreen>().planetId
            PlanetDetailsPage(planetId = planetId, onNavigateBack = {
                navController.navigateUp()
            })
        }
    }
}