package com.example.weatherapp.screens.forecast.model

import androidx.annotation.StringRes

internal sealed interface ForecastStateUi {
    data object Loading : ForecastStateUi

    data class Content(
        val cityName: String?,
        val current: CurrentWeatherUi,
        val daily: List<DailyWeatherUi>,
        val showLocationPermissionRationale: Boolean,
    ) : ForecastStateUi

    data class Message(
        @field:StringRes val messageId: Int,
    ) : ForecastStateUi
}

data class CurrentWeatherUi(
    val temp: String,
    val feelsLike: String,
    val pressure: String,
    val humidity: String,
    val windSpeed: String,
    val iconUrl: String?,
    val description: String?,
)

data class DailyWeatherUi(
    val day: String,
    val minTemp: String,
    val maxTemp: String,
    val iconUrl: String?,
)