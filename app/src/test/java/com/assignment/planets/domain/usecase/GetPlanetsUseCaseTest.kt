package com.assignment.planets.domain.usecase

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.assignment.planets.data.repository.PlanetsRepository
import com.assignment.planets.domain.model.PlanetData
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class GetPlanetsUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: PlanetsRepository
    private lateinit var useCase: GetPlanetsUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        useCase = GetPlanetsUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke emits expected PagingData list`() = runTest {

        val planets = listOf(
            PlanetData(1, "Tatooine", "Arid", "1g", "url1", "304"),
            PlanetData(2, "Dagobah", "Swamp", "0.9g", "url2", "341")
        )

        val pagingData = PagingData.from(planets)

        coEvery { repository.getPlanets() } returns flowOf(pagingData)

        val differ = AsyncPagingDataDiffer(
            diffCallback = PlanetDiffCallback(),
            updateCallback = NoActionListCallback,
            mainDispatcher = testDispatcher,
            workerDispatcher = testDispatcher
        )

        val job = launch {
            useCase().collect {
                differ.submitData(it)
            }
        }

        advanceUntilIdle()

        // Then
        assertEquals(2, differ.itemCount)
        assertEquals("Tatooine", differ.getItem(0)?.name)
        assertEquals("Dagobah", differ.getItem(1)?.name)

        job.cancel()
    }

    // Dummy diff callback for test
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

    @Test
    fun `invoke fails with IOException when repository throws`() = runTest {
        val ioException = IOException("No internet")

        coEvery { repository.getPlanets() } returns flow { throw ioException }

        val useCase = GetPlanetsUseCase(repository)

        val exception = assertFailsWith<IOException> {
            useCase().collect {
            }
        }

        assertEquals("No internet", exception.message)
    }


}
