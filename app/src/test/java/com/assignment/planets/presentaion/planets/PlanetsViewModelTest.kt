package com.assignment.planets.presentaion.planets

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.assignment.planets.domain.model.PlanetData
import com.assignment.planets.domain.usecase.GetPlanetsUseCase
import com.assignment.planets.ui.feature.planets.PlanetsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

@ExperimentalCoroutinesApi
class PlanetsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getPlanetsUseCase: GetPlanetsUseCase
    private lateinit var viewModel: PlanetsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getPlanetsUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `planetFlow emits paging data from use case`() = runTest {
        val dummyList = listOf(
            PlanetData(1, "Tatooine", "Arid", "1 standard", "url", "304")
        )

        val pagingData = PagingData.from(dummyList)

        coEvery { getPlanetsUseCase.invoke() } returns flowOf(pagingData)

        viewModel = PlanetsViewModel(getPlanetsUseCase)

        val differ = AsyncPagingDataDiffer(
            diffCallback = PlanetDiffCallback(),
            updateCallback = DoNothingListCallback,
            workerDispatcher = testDispatcher
        )

        val job = launch {
            viewModel.planetFlow.collect {
                differ.submitData(it)
            }
        }

        advanceUntilIdle()

        assertEquals(1, differ.itemCount)
        assertEquals("Tatooine", differ.getItem(0)?.name)

        job.cancel()
    }

    class PlanetDiffCallback : DiffUtil.ItemCallback<PlanetData>() {
        override fun areItemsTheSame(oldItem: PlanetData, newItem: PlanetData): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PlanetData, newItem: PlanetData): Boolean =
            oldItem == newItem
    }

    // dummy callback to notify actions
    object DoNothingListCallback : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}
