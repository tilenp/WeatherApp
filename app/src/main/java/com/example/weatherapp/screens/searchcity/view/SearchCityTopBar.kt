@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.weatherapp.screens.searchcity.view

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.Sizes
import com.example.weatherapp.ui.theme.WeatherAppTheme

@Composable
fun SearchCityTopBar(
    searchInput: TextFieldValue,
    onSearchInputChange: (TextFieldValue) -> Unit,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
) {
    TopAppBar(
        navigationIcon = {
            NavigationIcon(
                onBackClick = onBackClick,
            )
        },
        title = {
            InputView(
                searchInput = searchInput,
                onSearchInputChange = onSearchInputChange,
            )
        },
        actions = {
            if (searchInput.text.isNotBlank()) {
                ActionIcon(
                    onSearchIconClick = onSearchClick,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
private fun NavigationIcon(
    onBackClick: () -> Unit,
) {
    IconButton(
        onClick = onBackClick,
    ) {
        Icon(
            modifier = Modifier.size(Sizes.smallIcon),
            imageVector = Icons.Filled.ChevronLeft,
            contentDescription = stringResource(id = R.string.back_button),
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun InputView(
    searchInput: TextFieldValue,
    onSearchInputChange: (TextFieldValue) -> Unit,
) {
    TextField(
        value = searchInput,
        onValueChange = onSearchInputChange,
        placeholder = {
            Text(stringResource(R.string.search_hint))
        },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            // Remove background
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            // Remove the underline/indicator
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        )
    )
}

@Composable
private fun ActionIcon(
    onSearchIconClick: () -> Unit,
) {
    IconButton(onClick = { onSearchIconClick() }) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(id = R.string.search_city_icon),
            tint = MaterialTheme.colorScheme.onSurface
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
private fun SearchCityTopBarPreview() {
    WeatherAppTheme {
        SearchCityTopBar(
            searchInput = TextFieldValue(""),
            onBackClick = {},
            onSearchClick = {},
            onSearchInputChange = {}
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
private fun SearchCityTopBarWithInputPreview() {
    WeatherAppTheme {
        SearchCityTopBar(
            searchInput = TextFieldValue("London"),
            onBackClick = {},
            onSearchClick = {},
            onSearchInputChange = {}
        )
    }
}