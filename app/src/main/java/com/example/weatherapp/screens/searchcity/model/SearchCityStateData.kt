package com.example.weatherapp.screens.searchcity.model

import androidx.annotation.StringRes
import androidx.compose.ui.text.input.TextFieldValue
import com.example.domain.model.GeocodingItem

internal data class SearchCityStateData(
    val isLoading: Boolean = false,
    val searchInput: TextFieldValue = TextFieldValue(""),
    val geocodingItems: List<GeocodingItem>? = null,
    @field:StringRes val messageId: Int? = null,
)