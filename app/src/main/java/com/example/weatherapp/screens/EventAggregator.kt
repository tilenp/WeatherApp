package com.example.weatherapp.screens

import com.example.domain.model.GeocodingItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

internal class EventAggregator {

    private val _selectedGeocodingItem = MutableSharedFlow<GeocodingItem>(replay = 1)
    val selectedGeocodingItem: SharedFlow<GeocodingItem> = _selectedGeocodingItem

    suspend fun setSelectedGeocodingItem(geocodingItem: GeocodingItem) {
        _selectedGeocodingItem.emit(geocodingItem)
    }
}

