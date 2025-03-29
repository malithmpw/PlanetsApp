package com.assignment.planets.data.repository.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "planets")
data class PlanetEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val climate: String,
    val image: String?,
    val orbital_period: String,
    val gravity: String,
)