package com.example.data.model

import androidx.annotation.Keep

@Keep
internal data class GeocodingItemDto(
    val name: String?,
    val localNames: Map<String, String?>?,
    val lat: Double?,
    val lon: Double?,
    val country: String?,
    val state: String?,
)