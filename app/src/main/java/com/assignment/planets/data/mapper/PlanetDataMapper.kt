package com.assignment.planets.data.mapper

import com.assignment.planets.data.repository.local.PlanetEntity
import com.assignment.planets.domain.model.PlanetData

fun PlanetEntity.toPlanetData(): PlanetData {
    return PlanetData(id, name, climate, image, orbital_period, gravity)
}