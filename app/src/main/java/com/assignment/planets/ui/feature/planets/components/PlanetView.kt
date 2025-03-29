package com.assignment.planets.ui.feature.planets.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.assignment.planets.R
import com.assignment.planets.domain.model.PlanetData
import com.assignment.planets.ui.feature.common.components.LoadImage
import com.assignment.planets.ui.feature.common.util.withSize
import com.assignment.planets.ui.theme.Typography

@Composable
fun PlanetView(
    modifier: Modifier = Modifier,
    planet: PlanetData,
    onPlanetClick: (Int) -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .padding(top = 2.dp)
            .fillMaxWidth()
            .background(color = Color.White),
        shape = RoundedCornerShape(
            8.dp
        ), elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .clickable {
                    onPlanetClick.invoke(planet.id)
                }
        ) {
            Text(
                modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 4.dp),
                text = planet.name,
                maxLines = 2,
                style = Typography.bodyLarge,
                color = Color.Black
            )

            Box(modifier = Modifier.padding(8.dp)) {
                LoadImage(modifier = Modifier, url = planet.image?.withSize(250, 500))
            }

            Row {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = stringResource(R.string.climate_label_with_colon),
                    style = Typography.labelLarge,
                    color = Color.Black,
                )

                Text(
                    modifier = Modifier.padding(8.dp),
                    text = planet.climate,
                    style = Typography.labelLarge,
                    color = Color.Black,
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewPlanetView() {
    PlanetView(
        planet = PlanetData(
            1,
            "Name",
            "temperate",
            "image",
            orbital_period = "", gravity = ""
        ),
        onPlanetClick = {})
}