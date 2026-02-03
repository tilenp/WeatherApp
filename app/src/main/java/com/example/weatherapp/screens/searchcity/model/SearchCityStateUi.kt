package com.example.weatherapp.screens.searchcity.model

import androidx.annotation.StringRes
import androidx.compose.ui.text.input.TextFieldValue
import com.example.domain.model.GeocodingItem

internal sealed interface SearchCityStateUi {
    data object Loading : SearchCityStateUi

    data class Content(
        val searchInput: TextFieldValue,
        val geocodingItems: List<GeocodingItem>,
        val searchButton: ButtonUi,
    ) : SearchCityStateUi

    data class Message(
        @field:StringRes val messageId: Int,
    ) : SearchCityStateUi
}

internal data class ButtonUi(
    val isEnabled: Boolean,
)