package com.assignment.planets.data.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PlanetEntity::class],
    version = 1
)
abstract class PlanetsDatabase : RoomDatabase() {
    abstract val planetsDao: PlanetsDao
}