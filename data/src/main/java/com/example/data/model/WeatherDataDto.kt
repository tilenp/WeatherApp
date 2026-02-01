package com.example.data.model

import androidx.annotation.Keep

@Keep
internal data class WeatherDataDto(
    val current: CurrentWeatherDto?,
    val daily: List<DailyWeatherDto?>?
)

@Keep
internal data class CurrentWeatherDto(
    val dt: Long?,
    val temp: Double?,
    val feelsLike: Double?,
    val pressure: Int?,
    val humidity: Int?,
    val windSpeed: Double?,
    val weather: List<WeatherDto>?
)

@Keep
internal data class DailyWeatherDto(
    val dt: Long?,
    val temp: TemperatureDto?,
    val weather: List<WeatherDto>?,
)

@Keep
internal data class TemperatureDto(
    val min: Double?,
    val max: Double?,
)

@Keep
internal data class WeatherDto(
    val id: Int?,
    val description: String?,
    val icon: String?,
)