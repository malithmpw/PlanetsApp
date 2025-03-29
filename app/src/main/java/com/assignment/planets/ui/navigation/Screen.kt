package com.assignment.planets.ui.navigation

import kotlinx.serialization.Serializable


@Serializable
data object SplashScreen

@Serializable
data object PlanetsScreen

@Serializable
data class PlanetDetailsScreen(val planetId: Int)
