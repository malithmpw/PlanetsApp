package com.assignment.planets.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.assignment.planets.data.mapper.toPlanetData
import com.assignment.planets.data.repository.PlanetsRepository
import com.assignment.planets.data.repository.local.PlanetEntity
import com.assignment.planets.domain.model.PlanetData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPlanetsUseCase @Inject constructor(private val repository: PlanetsRepository) {
    operator fun invoke(): Flow<PagingData<PlanetData>> = repository.getPlanets().flowOn(Dispatchers.IO)
}