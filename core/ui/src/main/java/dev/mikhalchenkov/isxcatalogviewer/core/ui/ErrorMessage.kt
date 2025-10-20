package dev.mikhalchenkov.isxcatalogviewer.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.mikhalchenkov.isxcatalogviewer.core.ui.theme.ISXCatalogViewerTheme

@Composable
fun ErrorMessage(
    message: String,
    onButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
    buttonText: String? = null,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onButtonClicked) {
            Text(text = buttonText ?: stringResource(R.string.retry_btn))
        }
    }
}

@Preview
@Composable
private fun ErrorMessagePreview() {
    ISXCatalogViewerTheme {
        ErrorMessage(
            message = "An error occurred",
            onButtonClicked = {}
        )
    }
}