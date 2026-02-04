package com.example.weatherapp.screens.searchcity.view

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.domain.model.GeocodingItem
import com.example.domain.model.LatLng
import com.example.weatherapp.R
import com.example.weatherapp.common.MessageView
import com.example.weatherapp.screens.searchcity.model.ButtonUi
import com.example.weatherapp.screens.searchcity.model.SearchCityStateUi
import com.example.weatherapp.ui.theme.Dimens
import com.example.weatherapp.ui.theme.WeatherAppTheme

@Composable
internal fun SearchCityContentView(
    stateUi: SearchCityStateUi.Content,
    onSearchInputChange: (TextFieldValue) -> Unit,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    onGeocodingItemSelected: (GeocodingItem) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SearchCityTopBar(
                searchInput = stateUi.searchInput,
                onSearchInputChange = onSearchInputChange,
                onBackClick = onBackClick,
                onSearchClick = onSearchClick,
            )
        },
    ) { padding ->
        when {
            stateUi.messageId != null -> MessageView(
                modifier = Modifier.fillMaxSize(),
                message = stringResource(id = stateUi.messageId)
            )
            stateUi.geocodingItems?.isNotEmpty() == true -> CitiesView(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = Dimens.spacing16),
                geocodingItems = stateUi.geocodingItems,
                onGeocodingItemSelected = onGeocodingItemSelected,
            )
            stateUi.geocodingItems?.isEmpty() == true ->  MessageView(
                modifier = Modifier.fillMaxSize(),
                message = stringResource(id = R.string.no_results_try_another_city)
            )
        }
    }
}

@Composable
private fun CitiesView(
    modifier: Modifier,
    geocodingItems: List<GeocodingItem>,
    onGeocodingItemSelected: (GeocodingItem) -> Unit
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        geocodingItems.forEachIndexed { index, item ->
            CityView(
                geocodingItem = item,
                onGeocodingItemSelected = onGeocodingItemSelected,
            )
            if (index != geocodingItems.lastIndex) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f)
                )
            }
        }
    }
}

@Composable
private fun CityView(
    geocodingItem: GeocodingItem,
    onGeocodingItemSelected: (GeocodingItem) -> Unit,
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onGeocodingItemSelected(geocodingItem) }
            .padding(vertical = Dimens.spacing16),
        text = geocodingItem.getCityName(),
        style = MaterialTheme.typography.bodyLarge,
    )
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
internal fun SearchCityContentViewPreview() {
    WeatherAppTheme {
        SearchCityContentView(
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
                searchButton = ButtonUi(isEnabled = true)
            ),
            onSearchInputChange = {},
            onBackClick = {},
            onSearchClick = {},
            onGeocodingItemSelected = {},
        )
    }
}