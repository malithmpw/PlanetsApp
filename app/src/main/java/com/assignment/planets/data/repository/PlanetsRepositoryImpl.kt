package com.assignment.planets.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.assignment.planets.data.mapper.toPlanetData
import com.assignment.planets.data.repository.local.PlanetsDatabase
import com.assignment.planets.data.repository.remote.PlanetRemoteMediator
import com.assignment.planets.data.repository.remote.PlanetsApi
import com.assignment.planets.domain.model.PlanetData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class PlanetsRepositoryImpl(
    private val service: PlanetsApi,
    private val db: PlanetsDatabase
) : PlanetsRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPlanets(): Flow<PagingData<PlanetData>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = PlanetRemoteMediator(service, db),
            pagingSourceFactory = { db.planetsDao.getAllPlanets() }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                entity.toPlanetData()
            }
        }.flowOn(Dispatchers.Default) // if there is any heavy mapping,
        // mapping part will be done on Default dispatcher
    }

    /**
     * returns the PlanetData object for given id, if not found,
     * returns null
     */
    override fun getPlanet(planetId: Int): PlanetData? {
        return db.planetsDao.getPlanetById(planetId)?.toPlanetData()
    }
}