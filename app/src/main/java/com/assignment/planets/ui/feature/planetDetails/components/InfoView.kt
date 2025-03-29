package com.assignment.planets.ui.feature.planetDetails.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.assignment.planets.ui.theme.Typography

@Composable
fun PlanetInfoView(modifier: Modifier = Modifier, label: String, value: String) {
    Row(modifier = modifier.padding(top = 16.dp)) {
        Text(
            modifier = Modifier
                .padding(end = 16.dp)
                .width(180.dp),
            text = label,
            style = Typography.bodyLarge,
            color = Color.Black,
        )

        Text(
            modifier = Modifier,
            text = value,
            style = Typography.labelLarge,
            color = Color.Black,
        )
    }
}

@Preview
@Composable
fun PreviewPlanetInfoView() {
    PlanetInfoView(modifier = Modifier, "Label", "Info")
}