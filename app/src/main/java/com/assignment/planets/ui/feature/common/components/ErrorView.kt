package com.assignment.planets.ui.feature.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.assignment.planets.R
import com.assignment.planets.ui.theme.Typography


@Composable
fun ErrorView(
    modifier: Modifier = Modifier,
    showRetryButton: Boolean = false,
    message: String,
    onRetry: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.planet_404),
                contentDescription = null,
                Modifier.size(250.dp)
            )
            Text(
                text = message,
                style = Typography.bodyLarge.copy(textAlign = TextAlign.Center),
                color = Color.Black,
                maxLines = 2,
                modifier = Modifier.fillMaxWidth(),
            )

            if (showRetryButton) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { onRetry.invoke() }) {
                    Text(text = stringResource(R.string.retry))
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewErrorView() {
    ErrorView(message = "Message", showRetryButton = true)
}