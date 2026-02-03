@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.weatherapp.screens.forecast.view

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.Dimens
import com.example.weatherapp.ui.theme.Sizes
import com.example.weatherapp.ui.theme.WeatherAppTheme

@Composable
internal fun ForecastTopBar(
    cityName: String?,
    onSearchIconClick: () -> Unit,
    onLoadForecastForMyLocationClick: () -> Unit,
) {
    TopAppBar(
        modifier = Modifier.shadow(Dimens.spacing6),
        title = {
            Title(cityName = cityName)
        },
        actions = {
            ActionIcons(
                onSearchIconClick = onSearchIconClick,
                onLoadForecastForMyLocationClick = onLoadForecastForMyLocationClick,
            )
        }
    )
}

@Composable
private fun Title(
    cityName: String?,
) {
    Text(
        text = cityName ?: stringResource(R.string.unknown_city),
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
private fun ActionIcons(
    onSearchIconClick: () -> Unit,
    onLoadForecastForMyLocationClick: () -> Unit,
) {
    Row {
        IconButton(onClick = onSearchIconClick) {
            Icon(
                modifier = Modifier.size(Sizes.smallIcon),
                imageVector = Icons.Outlined.Search,
                contentDescription = stringResource(id = R.string.search_city_icon),
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
        IconButton(onClick = onLoadForecastForMyLocationClick) {
            Icon(
                modifier = Modifier.size(Sizes.smallIcon),
                imageVector = Icons.Default.MyLocation,
                contentDescription = stringResource(id = R.string.search_city_icon),
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
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
private fun ForecastTopBarPreview() {
    WeatherAppTheme {
        ForecastTopBar(
            cityName = "London",
            onSearchIconClick = {},
            onLoadForecastForMyLocationClick = {}
        )
    }
}