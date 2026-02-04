package com.example.weatherapp.screens.searchcity.mapper

import androidx.compose.ui.text.input.TextFieldValue
import com.example.domain.model.GeocodingItem
import com.example.domain.model.LatLng
import com.example.weatherapp.R
import com.example.weatherapp.screens.searchcity.model.SearchCityStateData
import com.example.weatherapp.screens.searchcity.model.SearchCityStateUi
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SearchCityStateUiMapperTest {

    private val mapper = SearchCityStateUiMapper()

    @Test
    fun `map returns Loading when isLoading is true`() {
        val stateData = SearchCityStateData(isLoading = true)

        val result = mapper.map(stateData)

        assertTrue(result is SearchCityStateUi.Loading)
    }

    @Test
    fun `map returns Message when messageId is not null`() {
        val stateData = SearchCityStateData(
            messageId = 123,
            searchInput = TextFieldValue("query")
        )

        val result = mapper.map(stateData)

        assertTrue(result is SearchCityStateUi.Content.Message)
        val message = result as SearchCityStateUi.Content.Message
        assertEquals("query", message.searchInput.text)
        assertEquals(123, message.messageId)
    }

    @Test
    fun `map returns Message with no_results_try_another_city when geocodingItems is empty`() {
        val stateData = SearchCityStateData(
            geocodingItems = emptyList(),
            searchInput = TextFieldValue("query")
        )

        val result = mapper.map(stateData)

        assertTrue(result is SearchCityStateUi.Content.Message)
        val message = result as SearchCityStateUi.Content.Message
        assertEquals(R.string.no_results_try_another_city, message.messageId)
        assertEquals("query", message.searchInput.text)
    }

    @Test
    fun `map returns GeocodingItems when geocodingItems is not empty`() {
        val items = listOf(
            GeocodingItem(
                name = "City",
                latLng = LatLng(0.0, 0.0),
                country = "country",
                state = null
            )
        )
        val stateData = SearchCityStateData(
            geocodingItems = items,
            searchInput = TextFieldValue("query")
        )

        val result = mapper.map(stateData)

        assertTrue(result is SearchCityStateUi.Content.GeocodingItems)
        val geocoding = result as SearchCityStateUi.Content.GeocodingItems
        assertEquals(items, geocoding.geocodingItems)
        assertEquals("query", geocoding.searchInput.text)
    }

    @Test
    fun `map returns Empty when geocoding items and message are null`() {
        val stateData = SearchCityStateData()

        val result = mapper.map(stateData)

        assertTrue(result is SearchCityStateUi.Content.Empty)
    }
}
