package com.assignment.planets.ui.feature.planets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.assignment.planets.domain.usecase.GetPlanetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlanetsViewModel @Inject constructor(planetsUseCase: GetPlanetsUseCase) :
    ViewModel() {
    val planetFlow = planetsUseCase.invoke()
        .cachedIn(viewModelScope)
}