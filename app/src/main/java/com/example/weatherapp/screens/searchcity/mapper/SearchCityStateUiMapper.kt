package com.example.weatherapp.screens.searchcity.mapper

import com.example.weatherapp.screens.searchcity.model.ButtonUi
import com.example.weatherapp.screens.searchcity.model.SearchCityStateData
import com.example.weatherapp.screens.searchcity.model.SearchCityStateUi
import javax.inject.Inject

internal class SearchCityStateUiMapper @Inject constructor() {

    fun map(
        stateData: SearchCityStateData,
    ): SearchCityStateUi {
        return when {
            stateData.isLoading -> SearchCityStateUi.Loading
            stateData.messageId != null -> SearchCityStateUi.Message(messageId = stateData.messageId)
            else -> SearchCityStateUi.Content(
                searchInput = stateData.searchInput,
                geocodingItems = stateData.geocodingItems,
                searchButton = mapSearchButton(stateData = stateData),
            )
        }
    }

    private fun mapSearchButton(stateData: SearchCityStateData): ButtonUi {
        val isInputValid = stateData.searchInput.text.isNotEmpty()
        val noMessage = stateData.messageId == null
        return ButtonUi(isEnabled = isInputValid && noMessage)
    }
}