package com.assignment.planets.data.repository

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.assignment.planets.data.mapper.toPlanetData
import com.assignment.planets.data.repository.local.PlanetEntity
import com.assignment.planets.data.repository.local.PlanetsDao
import com.assignment.planets.data.repository.local.PlanetsDatabase
import com.assignment.planets.data.repository.remote.PlanetsApi
import com.assignment.planets.domain.model.PlanetData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class PlanetsRepositoryImplTest {

    private lateinit var repository: PlanetsRepositoryImpl
    private val db: PlanetsDatabase = mockk()
    private val dao: PlanetsDao = mockk()
    private val service: PlanetsApi = mockk()

    @Before
    fun setup() {
        every { db.planetsDao } returns dao
    }

    @Test
    fun `getPlanet returns expected PlanetData`() = runTest {
        val planetId = 1
        val entity = PlanetEntity(
            id = planetId,
            name = "Tatooine",
            climate = "arid",
            gravity = "1g",
            image = "img_url",
            orbital_period = "304"
        )

        coEvery { dao.getPlanetById(planetId) } returns entity

        repository = PlanetsRepositoryImpl(service, db)

        val result = repository.getPlanet(planetId)

        assertEquals(entity.toPlanetData(), result)
        coVerify(exactly = 1) { dao.getPlanetById(planetId) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getPlanets emits PagingData from local source`() = runTest {
        val scheduler = TestCoroutineScheduler()
        val testDispatcher = StandardTestDispatcher(scheduler)

        Dispatchers.setMain(testDispatcher)

        val planets = listOf(
            PlanetData(1, "Tatooine", "arid", "1g", "url1", "304"),
            PlanetData(2, "Dagobah", "swamp", "0.9g", "url2", "341")
        )

        val pagingData = PagingData.from(planets)

        val differ = AsyncPagingDataDiffer(
            diffCallback = object : DiffUtil.ItemCallback<PlanetData>() {
                override fun areItemsTheSame(oldItem: PlanetData, newItem: PlanetData): Boolean = oldItem.id == newItem.id
                override fun areContentsTheSame(oldItem: PlanetData, newItem: PlanetData): Boolean = oldItem == newItem
            },
            updateCallback = NoActionListCallback,
            mainDispatcher = testDispatcher,
            workerDispatcher = testDispatcher
        )

        val job = launch(testDispatcher) {
            differ.submitData(pagingData)
        }

        advanceUntilIdle()

        assertEquals(2, differ.itemCount)
        assertEquals("Tatooine", differ.getItem(0)?.name)
        assertEquals("Dagobah", differ.getItem(1)?.name)

        job.cancel()

        Dispatchers.resetMain()
    }


    class PlanetDiffCallback : DiffUtil.ItemCallback<PlanetData>() {
        override fun areItemsTheSame(oldItem: PlanetData, newItem: PlanetData): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PlanetData, newItem: PlanetData): Boolean =
            oldItem == newItem
    }

    object NoActionListCallback : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}
