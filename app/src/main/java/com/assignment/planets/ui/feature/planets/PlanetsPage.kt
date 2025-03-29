package com.assignment.planets.ui.feature.planets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.assignment.planets.R
import com.assignment.planets.ui.feature.common.components.ErrorView
import com.assignment.planets.ui.feature.common.util.BackPressHandler
import com.assignment.planets.ui.feature.common.util.getMessage
import com.assignment.planets.ui.feature.planets.components.PlanetView
import com.assignment.planets.ui.theme.Typography
import com.assignment.planets.ui.theme.primary_planet_dark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanetsPage(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    goToPlantDetails: (Int) -> Unit
) {
    val viewModel: PlanetsViewModel = hiltViewModel()
    val planetItems = viewModel.planetFlow.collectAsLazyPagingItems()

    BackPressHandler {
        onBackPressed.invoke()
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeContent,
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.planets),
                        style = Typography.headlineMedium,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(containerColor = primary_planet_dark)
            )
        }) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (planetItems.loadState.refresh) {
                is LoadState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is LoadState.Error -> {
                    val e = planetItems.loadState.refresh as LoadState.Error
                    ErrorView(
                        modifier = Modifier.fillMaxSize(),
                        message = stringResource(e.getMessage()),
                        showRetryButton = true,
                        onRetry = { planetItems.retry() })
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        items(planetItems.itemCount) { index ->
                            planetItems[index]?.let {
                                PlanetView(
                                    modifier = Modifier.padding(bottom = 16.dp),
                                    planet = it
                                ) { planetId ->
                                    goToPlantDetails.invoke(planetId)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}