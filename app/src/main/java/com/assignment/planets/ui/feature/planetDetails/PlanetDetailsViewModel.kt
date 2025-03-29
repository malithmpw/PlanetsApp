package com.assignment.planets.ui.feature.planetDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.planets.domain.usecase.GetPlanetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface PlanetDetailsAction {
    data class LoadPlanetDetails(val planetId: Int) : PlanetDetailsAction
}

@HiltViewModel
class PlanetDetailsViewModel @Inject constructor(private val planetUseCase: GetPlanetUseCase) :
    ViewModel() {

    private val _uiState = MutableStateFlow<PlanetDetailsUiState>(PlanetDetailsUiState.Loading)
    val uiState: StateFlow<PlanetDetailsUiState> = _uiState

    /**
     * onAction will delegate the work to corresponding function and update the UI state accordingly
     *
     * @param action will specify which action to be performed
     */
    fun onAction(action: PlanetDetailsAction) {
        when (action) {
            is PlanetDetailsAction.LoadPlanetDetails -> {
                getPlanet(action.planetId)
            }
        }
    }

    /**
     * @param planetId id of the planet to retrieve the planet data
     */
    private fun getPlanet(planetId: Int) {
        viewModelScope.launch {
            _uiState.update { PlanetDetailsUiState.Loading }

            planetUseCase.invoke(planetId).collect { planetData ->
                _uiState.update {
                    if (planetData == null) {
                        PlanetDetailsUiState.Error("No planet found")
                    } else {
                        PlanetDetailsUiState.Success(planetData)
                    }
                }
            }
        }
    }
}