package com.example.weatherapp.screens.searchcity.viewmodel

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.GeocodingItem
import com.example.domain.repository.GeocodingRepository
import com.example.weatherapp.screens.EventAggregator
import com.example.weatherapp.screens.searchcity.mapper.SearchCityStateUiMapper
import com.example.weatherapp.screens.searchcity.model.SearchCityStateData
import com.example.weatherapp.screens.searchcity.model.SearchCityStateUi
import com.example.weatherapp.utils.DispatcherProvider
import com.example.weatherapp.utils.ErrorMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
internal class SearchCityViewModel @Inject constructor(
    private val eventAggregator: EventAggregator,
    private val geocodingRepository: GeocodingRepository,
    private val searchCityStateUiMapper: SearchCityStateUiMapper,
    private val errorMapper: ErrorMapper,
    private val locale: Locale,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private val state = MutableStateFlow(SearchCityStateData())
    val stateUi = state
        .map { stateData -> searchCityStateUiMapper.map(stateData = stateData) }
        .stateIn(
            initialValue = SearchCityStateUi.Loading,
            scope = viewModelScope.plus(dispatcherProvider.main),
            started = SharingStarted.WhileSubscribed(5000)
        )

    fun onSearchInputChange(searchInput: TextFieldValue) {
        state.update { it.copy(searchInput = searchInput) }
    }

    fun onSearchClick() {
        state.update { it.copy(messageId = null) }
        viewModelScope.launch(dispatcherProvider.io) {
            state.update { it.copy(isLoading = true) }
            val address = state.value.searchInput.text
            geocodingRepository.getDirectGeocoding(address = address, locale = locale)
                .onSuccess { geocodingItems -> presentLocations(geocodingItems = geocodingItems) }
                .onFailure { throwable -> presentError(throwable = throwable) }
        }
    }

    private fun presentLocations(geocodingItems: List<GeocodingItem>) {
        state.update { it.copy(isLoading = false, geocodingItems = geocodingItems) }
    }

    private fun presentError(throwable: Throwable) {
        val errorMessageId = errorMapper.map(throwable = throwable)
        state.update { it.copy(isLoading = false, messageId = errorMessageId) }
    }

    fun onGeocodingItemSelected(geocodingItem: GeocodingItem) {
        viewModelScope.launch(dispatcherProvider.io) {
            eventAggregator.setSelectedGeocodingItem(geocodingItem = geocodingItem)
        }
    }
}