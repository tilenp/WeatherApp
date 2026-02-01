package com.example.domain.repository

import com.example.domain.model.LatLng

interface LocationRepository {
    suspend fun getCurrentLocation(): Result<LatLng>
}