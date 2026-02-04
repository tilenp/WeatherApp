@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.weatherapp.screens.searchcity.view

import android.content.res.Configuration
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.GeocodingItem
import com.example.domain.model.LatLng
import com.example.weatherapp.common.LoadingView
import com.example.weatherapp.screens.searchcity.model.ButtonUi
import com.example.weatherapp.screens.searchcity.model.SearchCityStateUi
import com.example.weatherapp.screens.searchcity.viewmodel.SearchCityViewModel
import com.example.weatherapp.ui.theme.WeatherAppTheme

@Composable
internal fun SearchCityScreen(
    viewModel: SearchCityViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val stateUi by viewModel.stateUi.collectAsStateWithLifecycle()
    SearchCityScreen(
        stateUi = stateUi,
        onSearchInputChange = viewModel::onSearchInputChange,
        onBackClick = onBackClick,
        onSearchClick = viewModel::onSearchClick,
        onGeocodingItemSelected = { geocodingItem ->
            viewModel.onGeocodingItemSelected(geocodingItem = geocodingItem)
            onBackClick()
        },
    )
}

@Composable
private fun SearchCityScreen(
    stateUi: SearchCityStateUi,
    onSearchInputChange: (TextFieldValue) -> Unit,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    onGeocodingItemSelected: (GeocodingItem) -> Unit,
) {
    when (stateUi) {
        is SearchCityStateUi.Loading -> LoadingView()

        is SearchCityStateUi.Content -> SearchCityContentView(
            stateUi = stateUi,
            onSearchInputChange = onSearchInputChange,
            onBackClick = onBackClick,
            onSearchClick = onSearchClick,
            onGeocodingItemSelected = onGeocodingItemSelected,
        )
    }
}

@Preview(
    showBackground = true,
    name = "Light Mode"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
internal fun SearchCityScreenPreview() {
    WeatherAppTheme {
        SearchCityScreen(
            stateUi = SearchCityStateUi.Content(
                searchInput = TextFieldValue("London"),
                geocodingItems = listOf(
                    GeocodingItem(
                        name = "Berlin",
                        latLng = LatLng(
                            lat = 52.5200,
                            lon = 13.4050
                        ),
                        country = "DE",
                        state = "Berlin"
                    ),
                    GeocodingItem(
                        name = "Paris",
                        latLng = LatLng(
                            lat = 48.8566,
                            lon = 2.3522
                        ),
                        country = "FR",
                        state = "Île-de-France"
                    ),
                    GeocodingItem(
                        name = "New York",
                        latLng = LatLng(
                            lat = 40.7128,
                            lon = -74.0060
                        ),
                        country = "US",
                        state = "NY"
                    ),
                    GeocodingItem(
                        name = "Tokyo",
                        latLng = LatLng(
                            lat = 35.6895,
                            lon = 139.6917
                        ),
                        country = "JP",
                        state = null
                    ),
                    GeocodingItem(
                        name = "Sydney",
                        latLng = LatLng(
                            lat = -33.8688,
                            lon = 151.2093
                        ),
                        country = "AU",
                        state = "NSW"
                    )
                ),
                messageId = null,
                searchButton = ButtonUi(isEnabled = true),
            ),
            onSearchInputChange = {},
            onBackClick = {},
            onSearchClick = {},
            onGeocodingItemSelected = {},
        )
    }
}

@Preview(
    showBackground = true,
    name = "Light Mode"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
internal fun SearchCityScreenLoadingPreview() {
    WeatherAppTheme {
        SearchCityScreen(
            stateUi = SearchCityStateUi.Loading,
            onSearchInputChange = {},
            onBackClick = {},
            onSearchClick = {},
            onGeocodingItemSelected = {},
        )
    }
}

@Preview(
    showBackground = true,
    name = "Light Mode"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
internal fun SearchCityScreenMessagePreview() {
    WeatherAppTheme {
        SearchCityScreen(
            stateUi = SearchCityStateUi.Content(
                searchInput = TextFieldValue("Unknown City"),
                geocodingItems = emptyList(),
                messageId = null,
                searchButton = ButtonUi(isEnabled = true),

                ),
            onSearchInputChange = {},
            onBackClick = {},
            onSearchClick = {},
            onGeocodingItemSelected = {},
        )
    }
}