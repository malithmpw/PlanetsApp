package com.assignment.planets.domain.model

data class PlanetData(
    val id: Int,
    val name: String,
    val climate: String,
    val image: String?,
    val orbital_period: String,
    val gravity: String,
)