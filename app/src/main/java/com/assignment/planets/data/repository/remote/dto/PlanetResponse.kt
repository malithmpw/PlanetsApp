package com.assignment.planets.data.repository.remote.dto

data class PlanetResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Planet>
)
