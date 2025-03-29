package com.assignment.planets.data.repository

import androidx.paging.PagingData
import com.assignment.planets.data.repository.local.PlanetEntity
import com.assignment.planets.domain.model.PlanetData
import kotlinx.coroutines.flow.Flow

interface PlanetsRepository {
    fun getPlanets(): Flow<PagingData<PlanetData>>
    fun getPlanet(planetId: Int): PlanetData?
}