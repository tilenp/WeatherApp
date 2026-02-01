package com.example.domain.repository

import com.example.domain.model.LatLng
import com.example.domain.model.Units
import com.example.domain.model.WeatherData
import java.util.Locale

interface WeatherRepository {
    suspend fun getWeatherData(latLng: LatLng, units: Units, locale: Locale): Result<WeatherData>
}