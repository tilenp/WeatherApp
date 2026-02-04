@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.weatherapp.screens.forecast.view


import android.content.res.Configuration
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapp.common.ErrorView
import com.example.weatherapp.common.LoadingView
import com.example.weatherapp.screens.forecast.model.CurrentWeatherUi
import com.example.weatherapp.screens.forecast.model.DailyWeatherUi
import com.example.weatherapp.screens.forecast.model.ForecastStateUi
import com.example.weatherapp.screens.forecast.viewmodel.ForecastViewModel
import com.example.weatherapp.ui.theme.WeatherAppTheme

@Composable
internal fun ForecastScreen(
    viewModel: ForecastViewModel = hiltViewModel(),
    navigateToSearchCityDestination: () -> Unit,
) {
    val stateUi by viewModel.stateUi.collectAsStateWithLifecycle()
    ForecastScreen(
        stateUi = stateUi,
        onSearchIconClick = navigateToSearchCityDestination,
        onLoadForecastForMyLocationClick = viewModel::loadForecastForMyLocation,
        showLocationPermissionRationale = viewModel::showLocationPermissionRationale,
        onRetryClick = viewModel::onRetryClick,
    )
}

@Composable
private fun ForecastScreen(
    stateUi: ForecastStateUi,
    onSearchIconClick: () -> Unit,
    onLoadForecastForMyLocationClick: () -> Unit,
    showLocationPermissionRationale: (Boolean) -> Unit,
    onRetryClick: () -> Unit,
) {
    when (stateUi) {
        is ForecastStateUi.Loading -> LoadingView()

        is ForecastStateUi.Content -> ForecastContentView(
            stateUi = stateUi,
            onSearchIconClick = onSearchIconClick,
            onLoadForecastForMyLocationClick = onLoadForecastForMyLocationClick,
            showLocationPermissionRationale = showLocationPermissionRationale,
        )

        is ForecastStateUi.Message -> ErrorView(
            message = stringResource(id = stateUi.messageId),
            onRetryClick = onRetryClick,
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
private fun ForecastScreenPreview() {
    WeatherAppTheme {
        ForecastScreen(
            stateUi = ForecastStateUi.Content(
                cityName = "London",
                current = CurrentWeatherUi(
                    temp = "22°C",
                    feelsLike = "20°C",
                    pressure = "1013 hPa",
                    humidity = "65%",
                    windSpeed = "5 m/s",
                    iconUrl = null,
                    description = "Clear sky"
                ),
                daily = listOf(
                    DailyWeatherUi(
                        day = "Mon",
                        minTemp = "14°C",
                        maxTemp = "22°C",
                        iconUrl = ""
                    ),
                    DailyWeatherUi(
                        day = "Tue",
                        minTemp = "15°C",
                        maxTemp = "23°C",
                        iconUrl = null,
                    ),
                    DailyWeatherUi(
                        day = "Wed",
                        minTemp = "13°C",
                        maxTemp = "21°C",
                        iconUrl = null,
                    ),
                    DailyWeatherUi(
                        day = "Thu",
                        minTemp = "12°C",
                        maxTemp = "20°C",
                        iconUrl = null,
                    ),
                    DailyWeatherUi(
                        day = "Fri",
                        minTemp = "16°C",
                        maxTemp = "24°C",
                        iconUrl = null,
                    )
                ),
                showLocationPermissionRationale = false,
            ),
            onSearchIconClick = {},
            onLoadForecastForMyLocationClick = {},
            showLocationPermissionRationale = {},
            onRetryClick = {},
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
private fun ForecastScreenLoadingPreview() {
    WeatherAppTheme {
        ForecastScreen(
            stateUi = ForecastStateUi.Loading,
            onSearchIconClick = {},
            onLoadForecastForMyLocationClick = {},
            showLocationPermissionRationale = {},
            onRetryClick = {},
        )
    }
}