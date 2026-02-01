package com.example.domain.repository

import com.example.domain.model.GeocodingItem
import com.example.domain.model.LatLng
import java.util.Locale

interface GeocodingRepository {
    suspend fun getDirectGeocoding(
        address: String,
        locale: Locale,
    ): Result<List<GeocodingItem>>

    suspend fun getReverseGeocoding(
        latLng: LatLng,
        locale: Locale,
    ): Result<List<GeocodingItem>>
}