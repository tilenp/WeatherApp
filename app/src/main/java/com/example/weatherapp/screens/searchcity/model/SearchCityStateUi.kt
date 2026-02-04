package com.example.weatherapp.screens.searchcity.model

import androidx.annotation.StringRes
import androidx.compose.ui.text.input.TextFieldValue
import com.example.domain.model.GeocodingItem

internal sealed interface SearchCityStateUi {
    data object Loading : SearchCityStateUi

    sealed interface Content : SearchCityStateUi {
        val searchInput: TextFieldValue

        data class GeocodingItems(
            override val searchInput: TextFieldValue,
            val geocodingItems: List<GeocodingItem>,
        ) : Content

        data class Message(
            override val searchInput: TextFieldValue,
            @field:StringRes val messageId: Int,
        ) : Content

        data class Empty(
            override val searchInput: TextFieldValue,
        ) : Content
    }
}