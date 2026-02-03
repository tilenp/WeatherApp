package com.example.weatherapp.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.Dimens
import com.example.weatherapp.ui.theme.WeatherAppTheme

@Composable
internal fun ErrorView(
    message: String,
    onRetryClick: () -> Unit,
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(Dimens.spacing16)
        ) {
            MessageView(
                modifier = Modifier.weight(1f),
                message = message
            )
            ReloadButton(
                onRetryClick = onRetryClick,
            )
            Spacer(modifier = Modifier.height(Dimens.spacing16))
        }
    }
}

@Composable
private fun MessageView(
    modifier: Modifier = Modifier,
    message: String,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.spacing8),
            text = message,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ReloadButton(
    onRetryClick: () -> Unit
) {
    Button(
        onClick = onRetryClick,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.retry),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(
    showBackground = true,
    name = "Light Mode"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
private fun PreviewErrorView() {
    WeatherAppTheme {
        ErrorView(
            message = "Something went wrong",
            onRetryClick = {},
        )
    }
}