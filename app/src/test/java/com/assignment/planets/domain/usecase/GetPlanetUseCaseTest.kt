package com.assignment.planets.domain.usecase

import com.assignment.planets.data.repository.PlanetsRepository
import com.assignment.planets.domain.model.PlanetData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetPlanetUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: PlanetsRepository
    private lateinit var useCase: GetPlanetUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        useCase = GetPlanetUseCase(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `invoke returns PlanetData from repository`() = runTest {
        val planetId = 5
        val expected = PlanetData(
            id = planetId,
            name = "Dagobah",
            climate = "swamp",
            gravity = "0.9g",
            image = "image_url",
            orbital_period = "341"
        )

        coEvery { repository.getPlanet(planetId) } returns expected

        val result = useCase.invoke(planetId).first()

        assertEquals(expected, result)
        coVerify(exactly = 1) { repository.getPlanet(planetId) }
    }

    @Test
    fun `invoke returns no planet data from repository`() = runTest {

        val planetId = 5

        coEvery { repository.getPlanet(planetId) } returns null

        val result = useCase.invoke(planetId).first()

        assertEquals(null, result)
        coVerify(exactly = 1) { repository.getPlanet(planetId) }
    }
}
