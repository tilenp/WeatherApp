package com.example.data.mapper

import com.example.data.model.CurrentWeatherDto
import com.example.data.model.DailyWeatherDto
import com.example.data.model.TemperatureDto
import com.example.data.model.WeatherDataDto
import com.example.data.model.WeatherDto
import com.example.domain.model.CurrentWeather
import com.example.domain.model.DailyWeather
import com.example.domain.model.Temperature
import com.example.domain.model.Weather
import com.example.domain.model.WeatherData
import javax.inject.Inject

internal class WeatherDataMapper @Inject constructor() {

    fun map(dto: WeatherDataDto): WeatherData {
        return WeatherData(
            current = mapCurrent(dto.current),
            daily = dto.daily?.mapNotNull { mapDaily(dto = it) }.orEmpty()
        )
    }

    private fun mapCurrent(dto: CurrentWeatherDto?): CurrentWeather? {
        if (dto == null) return null
        val dt = dto.dt ?: return null

        return CurrentWeather(
            dt = dt,
            temp = dto.temp,
            feelsLike = dto.feelsLike,
            pressure = dto.pressure,
            humidity = dto.humidity,
            windSpeed = dto.windSpeed,
            weather = dto.weather?.mapNotNull { mapWeather(dto = it) }.orEmpty(),
        )
    }

    private fun mapDaily(dto: DailyWeatherDto?): DailyWeather? {
        if (dto == null) return null
        val dt = dto.dt ?: return null

        return DailyWeather(
            dt = dt,
            temp = mapTemperature(dto.temp),
            weather = dto.weather?.mapNotNull { mapWeather(dto = it) }.orEmpty(),
        )
    }

    private fun mapTemperature(dto: TemperatureDto?): Temperature? {
        if (dto == null) return null

        return Temperature(
            min = dto.min,
            max = dto.max,
        )
    }

    private fun mapWeather(dto: WeatherDto?): Weather? {
        if (dto == null) return null
        val id = dto.id ?: return null

        return Weather(
            id = id,
            description = dto.description,
            iconUrl = buildIconUrl(icon = dto.icon)
        )
    }

    private fun buildIconUrl(icon: String?): String? {
        if (icon == null) return null
        return "https://openweathermap.org/payload/api/media/file/$icon.png"
    }
}
