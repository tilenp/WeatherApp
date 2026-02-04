package com.example.weatherapp.screens.searchcity.mapper

import androidx.compose.ui.text.input.TextFieldValue
import com.example.domain.model.GeocodingItem
import com.example.domain.model.LatLng
import com.example.weatherapp.screens.searchcity.model.SearchCityStateData
import com.example.weatherapp.screens.searchcity.model.SearchCityStateUi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class SearchCityStateUiMapperTest {

    private val mapper = SearchCityStateUiMapper()

    @Test
    fun `map returns Loading when isLoading is true`() {
        val state = SearchCityStateData(
            isLoading = true,
            searchInput = TextFieldValue("London"),
            messageId = null
        )

        val result = mapper.map(state)

        assertTrue(result is SearchCityStateUi.Loading)
    }

    @Test
    fun `map returns Message when messageId is present`() {
        val state = SearchCityStateData(
            isLoading = false,
            searchInput = TextFieldValue("London"),
            messageId = 123
        )

        val result = mapper.map(state)

        assertTrue(result is SearchCityStateUi.Message)
        assertEquals(123, (result as SearchCityStateUi.Message).messageId)
    }

    @Test
    fun `map returns Content when not loading and no message`() {
        val searchInput = TextFieldValue("Berlin")
        val geocodingItems = listOf(
            GeocodingItem(
                name = "Berlin",
                latLng = LatLng(52.52, 13.405),
                country = "DE",
                state = null
            )
        )

        val state = SearchCityStateData(
            isLoading = false,
            searchInput = searchInput,
            geocodingItems = geocodingItems,
            messageId = null
        )

        val result = mapper.map(state)

        assertTrue(result is SearchCityStateUi.Content)

        val content = result as SearchCityStateUi.Content
        assertEquals(searchInput, content.searchInput)
        assertEquals(geocodingItems, content.geocodingItems)
    }

    @Test
    fun `search button is enabled when input is not empty and no message`() {
        val state = SearchCityStateData(
            searchInput = TextFieldValue("Paris"),
            messageId = null
        )

        val result = mapper.map(state) as SearchCityStateUi.Content

        assertTrue(result.searchButton.isEnabled)
    }

    @Test
    fun `search button is disabled when input is empty`() {
        val state = SearchCityStateData(
            searchInput = TextFieldValue(""),
            messageId = null
        )

        val result = mapper.map(state) as SearchCityStateUi.Content

        assertFalse(result.searchButton.isEnabled)
    }
}