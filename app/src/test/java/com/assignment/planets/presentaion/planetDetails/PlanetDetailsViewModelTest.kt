package com.assignment.planets.presentaion.planetDetails

import com.assignment.planets.domain.model.PlanetData
import com.assignment.planets.domain.usecase.GetPlanetUseCase
import com.assignment.planets.ui.feature.planetDetails.PlanetDetailsAction
import com.assignment.planets.ui.feature.planetDetails.PlanetDetailsUiState
import com.assignment.planets.ui.feature.planetDetails.PlanetDetailsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlanetDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getPlanetUseCase: GetPlanetUseCase
    private lateinit var viewModel: PlanetDetailsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getPlanetUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onAction emits Loading then Success when planet is found`() = runTest {
        val planetId = 1
        val planet = PlanetData(
            id = planetId,
            name = "Tatooine",
            climate = "arid",
            gravity = "1g",
            image = "img_url",
            orbital_period = "304"
        )
        coEvery { getPlanetUseCase.invoke(planetId) } returns flowOf(planet)

        viewModel = PlanetDetailsViewModel(getPlanetUseCase)

        viewModel.onAction(PlanetDetailsAction.LoadPlanetDetails(planetId))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is PlanetDetailsUiState.Success)
        assertEquals(planet, (state as PlanetDetailsUiState.Success).planet)
    }

    @Test
    fun `onAction emits Loading then Error when planet is not found`() = runTest {
        val planetId = 99
        coEvery { getPlanetUseCase.invoke(planetId) } returns flowOf(null)

        viewModel = PlanetDetailsViewModel(getPlanetUseCase)
        viewModel.onAction(PlanetDetailsAction.LoadPlanetDetails(planetId))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is PlanetDetailsUiState.Error)
        assertEquals("No planet found", (state as PlanetDetailsUiState.Error).message)
    }

    @Test
    fun `onAction immediately emits Loading state`() = runTest {
        val planetId = 5
        val flow = flow {
            emit(null)
        }
        coEvery { getPlanetUseCase.invoke(planetId) } returns flow

        viewModel = PlanetDetailsViewModel(getPlanetUseCase)

        viewModel.onAction(PlanetDetailsAction.LoadPlanetDetails(planetId))

        assertEquals(PlanetDetailsUiState.Loading, viewModel.uiState.value)
    }
}
