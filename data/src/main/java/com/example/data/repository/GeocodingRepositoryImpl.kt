package com.example.data.repository

import com.example.data.api.OpenWeatherMapApi
import com.example.data.mapper.GeocodingItemMapper
import com.example.domain.model.GeocodingItem
import com.example.domain.model.LatLng
import com.example.domain.repository.GeocodingRepository
import java.util.Locale
import javax.inject.Inject

internal class GeocodingRepositoryImpl @Inject constructor(
    private val api: OpenWeatherMapApi,
    private val geocodingItemMapper: GeocodingItemMapper,
) : GeocodingRepository {

    override suspend fun getDirectGeocoding(
        address: String,
        locale: Locale,
    ): Result<List<GeocodingItem>> = runCatching {
        val dtos = api.getDirectGeocoding(q = address, limit = 1)
        geocodingItemMapper.map(dtos = dtos, locale = locale)
    }

    override suspend fun getReverseGeocoding(
        latLng: LatLng,
        locale: Locale,
    ): Result<List<GeocodingItem>> = runCatching {
        val dtos = api.getReverseGeocoding(lat = latLng.lat, lon = latLng.lon, limit = 1)
        geocodingItemMapper.map(dtos = dtos, locale = locale)
    }
}