package com.example.domain.model

data class GeocodingItem(
    val name: String,
    val latLng: LatLng,
    val country: String,
    val state: String?,
)