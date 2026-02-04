package com.example.data.repository

import com.example.data.api.OpenWeatherMapApi
import com.example.data.mapper.WeatherDataMapper
import com.example.domain.model.LatLng
import com.example.domain.model.Units
import com.example.domain.model.WeatherData
import com.example.domain.repository.WeatherRepository
import java.util.Locale
import javax.inject.Inject

internal class WeatherRepositoryImpl @Inject constructor(
    private val api: OpenWeatherMapApi,
    private val weatherDataMapper: WeatherDataMapper,
) : WeatherRepository {

    override suspend fun getWeatherData(
        latLng: LatLng,
        units: Units,
        locale: Locale,
    ): Result<WeatherData> = runCatching {
        val exclude = "minutely,hourly,alerts"
        val dto = api.getWeatherData(
            lat = latLng.lat,
            lon = latLng.lon,
            exclude = exclude,
            units = units,
            lang = locale.language,
        )
        weatherDataMapper.map(dto = dto)
    }
}