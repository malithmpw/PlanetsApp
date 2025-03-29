package com.assignment.planets.ui.feature.planetDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.assignment.planets.R
import com.assignment.planets.domain.model.PlanetData
import com.assignment.planets.ui.feature.common.components.ErrorView
import com.assignment.planets.ui.feature.common.components.LoadImage
import com.assignment.planets.ui.feature.common.components.LoadingView
import com.assignment.planets.ui.feature.common.util.withSize
import com.assignment.planets.ui.feature.planetDetails.components.PlanetInfoView
import com.assignment.planets.ui.theme.Typography

@Composable
fun PlanetDetailsPage(modifier: Modifier = Modifier, onNavigateBack: () -> Unit, planetId: Int) {
    val viewModel: PlanetDetailsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onAction(PlanetDetailsAction.LoadPlanetDetails(planetId))
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeContent,
        modifier = modifier,
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (uiState) {
                is PlanetDetailsUiState.Loading -> {
                    LoadingView(Modifier.padding(innerPadding))
                }

                is PlanetDetailsUiState.Error -> {
                    ErrorView(message = stringResource(R.string.planet_not_found))
                }

                is PlanetDetailsUiState.Success -> {
                    PlanetDetailsContent(
                        modifier = Modifier,
                        onNavigateBack = onNavigateBack,
                        (uiState as PlanetDetailsUiState.Success).planet,
                    )
                }
            }
        }
    }
}

@Composable
fun PlanetDetailsContent(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    planet: PlanetData?
) {
    planet?.let {
        Column(
            modifier = modifier
                .background(color = Color.White)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    tint = Color.Black,
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            onNavigateBack.invoke()
                        }
                )
                Text(
                    modifier = Modifier,
                    text = it.name,
                    style = Typography.titleLarge,
                    color = Color.Black,
                    maxLines = 4,
                    textAlign = TextAlign.Center
                )
            }

            LoadImage(modifier = Modifier.padding(top = 16.dp), it.image?.withSize(800, 1600))

            PlanetInfoView(
                modifier = Modifier,
                stringResource(R.string.orbital_period_label),
                stringResource(R.string.orbital_period, planet.orbital_period)
            )
            PlanetInfoView(
                modifier = Modifier,
                stringResource(R.string.gravity_label),
                planet.gravity
            )
            PlanetInfoView(
                modifier = Modifier,
                stringResource(R.string.climate_label),
                planet.climate
            )
            Spacer(Modifier.height(120.dp))

        }
    }
}

@Preview
@Composable
fun PreviewPlanetDetails() {
    PlanetDetailsContent(
        modifier = Modifier,
        onNavigateBack = {},
        planet = PlanetData(
            1,
            "Earth",
            "climate",
            "image",
            orbital_period = "3667",
            gravity = "1"
        )
    )
}