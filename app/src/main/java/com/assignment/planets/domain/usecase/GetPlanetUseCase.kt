package com.assignment.planets.domain.usecase

import com.assignment.planets.data.repository.PlanetsRepository
import com.assignment.planets.domain.model.PlanetData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * GetPlanetUseCase returns a planetData object for given planetId if available,
 * else return null
 */
class GetPlanetUseCase @Inject constructor(private val repository: PlanetsRepository) {
    operator fun invoke(planetId: Int): Flow<PlanetData?> = flow {
        emit(repository.getPlanet(planetId = planetId))
    }.flowOn(Dispatchers.IO)
}