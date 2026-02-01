package com.example.domain.model

data class WeatherData(
    val current: CurrentWeather?,
    val daily: List<DailyWeather>,
)

data class CurrentWeather(
    val dt: Long,
    val temp: Double?,
    val feelsLike: Double?,
    val pressure: Int?,
    val humidity: Int?,
    val windSpeed: Double?,
    val weather: List<Weather>,
)

data class DailyWeather(
    val dt: Long,
    val temp: Temperature?,
    val weather: List<Weather>,
)

data class Temperature(
    val min: Double?,
    val max: Double?,
)

data class Weather(
    val id: Int,
    val description: String?,
    val iconUrl: String?,
)