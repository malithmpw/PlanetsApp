package com.assignment.planets.data.repository.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlanetsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(planets: List<PlanetEntity>)

    @Query("SELECT * FROM planets")
    fun getAllPlanets(): PagingSource<Int, PlanetEntity>

    @Query("SELECT * FROM planets WHERE id = :id")
    fun getPlanetById(id: Int): PlanetEntity?

    @Query("DELETE FROM planets")
    fun deleteAll()

    @Query("SELECT COUNT(id) FROM planets")
    fun getCount(): Int
}