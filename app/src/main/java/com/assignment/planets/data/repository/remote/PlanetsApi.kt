package com.assignment.planets.data.repository.remote

import com.assignment.planets.data.repository.remote.dto.PlanetResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PlanetsApi {
    @GET("planets/")
    suspend fun getPlanets(@Query("page") page: Int): PlanetResponse
}