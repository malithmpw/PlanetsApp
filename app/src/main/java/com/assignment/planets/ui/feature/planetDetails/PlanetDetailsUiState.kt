package com.assignment.planets.ui.feature.planetDetails

import com.assignment.planets.domain.model.PlanetData

sealed class PlanetDetailsUiState {
    data class Success(val planet: PlanetData?) : PlanetDetailsUiState()
    data object Loading : PlanetDetailsUiState()
    data class Error(val message: String) : PlanetDetailsUiState()
}