package com.example.data.repository

import com.example.domain.model.LatLng
import com.example.domain.repository.LocationRepository
import javax.inject.Inject

internal class LocationRepositoryImpl @Inject constructor() : LocationRepository {

    override suspend fun getCurrentLocation(): Result<LatLng> = runCatching {
        LatLng(
            lat = 51.5073219,
            lon = -0.1276474,
        )
    }
}