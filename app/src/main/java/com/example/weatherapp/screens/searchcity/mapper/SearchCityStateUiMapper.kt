package com.example.weatherapp.screens.searchcity.mapper

import com.example.weatherapp.R
import com.example.weatherapp.screens.searchcity.model.SearchCityStateData
import com.example.weatherapp.screens.searchcity.model.SearchCityStateUi
import javax.inject.Inject

internal class SearchCityStateUiMapper @Inject constructor() {

    fun map(
        stateData: SearchCityStateData,
    ): SearchCityStateUi {
        return when {
            stateData.isLoading -> SearchCityStateUi.Loading
            stateData.messageId != null -> SearchCityStateUi.Content.Message(
                searchInput = stateData.searchInput,
                messageId = stateData.messageId,
            )
            stateData.geocodingItems?.isEmpty() == true -> SearchCityStateUi.Content.Message(
                searchInput = stateData.searchInput,
                messageId = R.string.no_results_try_another_city,
            )
            stateData.geocodingItems?.isNotEmpty() == true -> SearchCityStateUi.Content.GeocodingItems(
                searchInput = stateData.searchInput,
                geocodingItems = stateData.geocodingItems,
            )
            else -> SearchCityStateUi.Content.Empty(
                searchInput = stateData.searchInput,
            )
        }
    }
}