package com.assignment.planets.data.repository.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.assignment.planets.BuildConfig
import com.assignment.planets.data.repository.local.PlanetEntity
import com.assignment.planets.data.repository.local.PlanetsDatabase

@OptIn(ExperimentalPagingApi::class)
class PlanetRemoteMediator(
    private val service: PlanetsApi,
    private val db: PlanetsDatabase
) : RemoteMediator<Int, PlanetEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PlanetEntity>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                if (lastItem == null) {
                    1
                } else {
                    (lastItem.id / state.config.pageSize) + 1
                }
            }
        }

        return try {
            val response = service.getPlanets(page)
            val entities = response.results.map {
                // "url": "https://swapi.dev/api/planets/1/"
                // based on the response, url contains unique value for each planet,
                // extract that id, so that, it will be used for image loading
                // id extracting part was only done because the response doesn't contain an id field.
                val id =
                    it.url.replace(BuildConfig.BASE_URL + "planets/", "").replace("/", "").toInt()
                val imageUrl = "${BuildConfig.IMAGE_BASE_URL}${id}/"
                PlanetEntity(
                    id = id,
                    name = it.name,
                    climate = it.climate,
                    image = imageUrl,
                    orbital_period = it.orbital_period,
                    gravity = it.gravity
                )
            }

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.planetsDao.deleteAll()
                }
                db.planetsDao.insertAll(entities)
            }

            MediatorResult.Success(endOfPaginationReached = response.next == null)
        } catch (e: Exception) {
            if (db.planetsDao.getCount() > 0) {
                MediatorResult.Success(endOfPaginationReached = true)
            } else {
                MediatorResult.Error(e)
            }
        }
    }
}
