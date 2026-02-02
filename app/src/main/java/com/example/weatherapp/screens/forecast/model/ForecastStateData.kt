package com.example.weatherapp.screens.forecast.model

import androidx.annotation.StringRes
import com.example.domain.model.WeatherData

internal data class ForecastStateData(
    val isLoading: Boolean = false,
    val cityName: String? = null,
    val weatherData: WeatherData? = null,
    val showLocationPermissionRationale: Boolean = false,
    @field:StringRes val messageId: Int? = null,
)