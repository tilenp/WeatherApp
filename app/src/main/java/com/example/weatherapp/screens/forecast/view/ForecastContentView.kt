package com.example.weatherapp.screens.forecast.view

import android.Manifest
import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.example.weatherapp.common.handleLocationPermission
import com.example.weatherapp.common.rememberLocationPermissionLauncher
import com.example.weatherapp.R
import com.example.weatherapp.screens.forecast.model.CurrentWeatherUi
import com.example.weatherapp.screens.forecast.model.DailyWeatherUi
import com.example.weatherapp.screens.forecast.model.ForecastStateUi
import com.example.weatherapp.ui.theme.Dimens
import com.example.weatherapp.ui.theme.Sizes
import com.example.weatherapp.ui.theme.WeatherAppTheme

@Composable
internal fun ForecastContentView(
    stateUi: ForecastStateUi.Content,
    onSearchIconClick: () -> Unit,
    onLoadForecastForMyLocationClick: () -> Unit,
    showLocationPermissionRationale: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    val activity = context as Activity

    val launcher = rememberLocationPermissionLauncher(
        onPermissionGranted = onLoadForecastForMyLocationClick,
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ForecastTopBar(
                cityName = stateUi.cityName,
                onSearchIconClick = onSearchIconClick,
                onLoadForecastForMyLocationClick = {
                    handleLocationPermission(
                        context = context,
                        activity = activity,
                        launcher = launcher,
                        onGranted = onLoadForecastForMyLocationClick,
                        showLocationPermissionRationale = showLocationPermissionRationale,
                    )
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(Dimens.spacing16))
            CurrentWeatherCard(currentWeather = stateUi.current)
            Spacer(modifier = Modifier.height(Dimens.spacing16))
            WeeklyForecast(items = stateUi.daily)
        }
        if (stateUi.showLocationPermissionRationale) {
            LocationPermissionRationaleDialog(
                onGrantPermission = {
                    showLocationPermissionRationale(false)
                    launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                },
                onDismiss = {
                    showLocationPermissionRationale(false)
                },
            )
        }
    }
}

@Composable
private fun CurrentWeatherCard(currentWeather: CurrentWeatherUi) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.spacing16),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.spacing0),
        shape = RoundedCornerShape(Dimens.spacing16),
    ) {
        Column(
            modifier = Modifier
                .padding(Dimens.spacing24)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            IconAndTemperature(currentWeather = currentWeather)
            WeatherDescription(description = currentWeather.description)
            Spacer(modifier = Modifier.height(Dimens.spacing24))
            FeelsLikeAndHumidity(currentWeather = currentWeather)
            Spacer(modifier = Modifier.height(Dimens.spacing8))
            WindAndPressure(currentWeather = currentWeather)
        }
    }
}

@Composable
private fun IconAndTemperature(currentWeather: CurrentWeatherUi) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = currentWeather.iconUrl,
            contentDescription = stringResource(id = R.string.weather_icon),
            error = painterResource(R.drawable.fallback_weather_icon),
            modifier = Modifier
                .size(Sizes.mediumIcon)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(Dimens.spacing16))
        Text(
            text = currentWeather.temp,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun WeatherDescription(description: String?) {
    if (description == null) return
    Spacer(modifier = Modifier.height(Dimens.spacing4))
    Text(
        text = description,
        style = MaterialTheme.typography.titleMedium,
    )
}

@Composable
private fun FeelsLikeAndHumidity(currentWeather: CurrentWeatherUi) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(id = R.string.feels_like, currentWeather.feelsLike),
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = stringResource(id = R.string.humidity, currentWeather.humidity),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun WindAndPressure(currentWeather: CurrentWeatherUi) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(id = R.string.wind_speed, currentWeather.windSpeed),
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = stringResource(id = R.string.pressure, currentWeather.pressure),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun WeeklyForecast(
    items: List<DailyWeatherUi>
) {
    if (items.isEmpty()) return
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.spacing16),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.spacing0),
        shape = RoundedCornerShape(Dimens.spacing16),
    ) {
        Column(
            modifier = Modifier.padding(Dimens.spacing16)
        ) {
            items.forEachIndexed { index, item ->
                DailyWeatherRow(dailyWeather = item)
                if (index != items.lastIndex) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f)
                    )
                }
            }
        }
    }
}

@Composable
private fun DailyWeatherRow(
    dailyWeather: DailyWeatherUi
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = dailyWeather.day,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.weight(1f))
        AsyncImage(
            model = dailyWeather.iconUrl,
            contentDescription = stringResource(R.string.weather_icon),
            error = painterResource(R.drawable.fallback_weather_icon),
            modifier = Modifier
                .size(Sizes.mediumIcon)
                .padding(horizontal = Dimens.spacing8)
        )
        Text(
            text = "${dailyWeather.maxTemp} / ${dailyWeather.minTemp}",
            style = MaterialTheme.typography.bodyMedium
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
private fun ForecastContentPreview() {
    WeatherAppTheme {
        ForecastContentView(
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
                        iconUrl = null,
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
        )
    }
}