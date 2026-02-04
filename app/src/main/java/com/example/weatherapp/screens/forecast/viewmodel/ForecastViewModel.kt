package com.example.weatherapp.screens.forecast.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.LatLng
import com.example.domain.model.Units
import com.example.domain.model.WeatherData
import com.example.domain.repository.GeocodingRepository
import com.example.domain.repository.LocationRepository
import com.example.domain.repository.WeatherRepository
import com.example.weatherapp.screens.EventAggregator
import com.example.weatherapp.screens.forecast.mapper.ForecastStateUiMapper
import com.example.weatherapp.screens.forecast.model.ForecastStateData
import com.example.weatherapp.screens.forecast.model.ForecastStateUi
import com.example.weatherapp.utils.DispatcherProvider
import com.example.weatherapp.utils.ErrorMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
internal class ForecastViewModel @Inject constructor(
    private val eventAggregator: EventAggregator,
    private val geocodingRepository: GeocodingRepository,
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository,
    private val forecastStateUiMapper: ForecastStateUiMapper,
    private val errorMapper: ErrorMapper,
    private val locale: Locale,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private val state = MutableStateFlow(ForecastStateData())
    val stateUi = state
        .map { stateData -> forecastStateUiMapper.map(stateData = stateData, units = Units.metric) }
        .stateIn(
            initialValue = ForecastStateUi.Loading,
            scope = viewModelScope.plus(dispatcherProvider.main),
            started = SharingStarted.WhileSubscribed(5000)
        )

    init {
        observeSelectedGeocodingItem()
    }

    private fun observeSelectedGeocodingItem() {
        viewModelScope.launch(dispatcherProvider.io) {
            eventAggregator.selectedGeocodingItem
                .onEach { geocodingItem -> state.update { it.copy(cityName = geocodingItem.name) } }
                .collect { geocodingItem -> loadWeatherData(latLng = geocodingItem.latLng) }
        }
    }

    private suspend fun loadWeatherData(latLng: LatLng) {
        state.update { it.copy(isLoading = true) }
        weatherRepository.getWeatherData(
            latLng = latLng,
            units = Units.metric,
            locale = locale,
        )
            .onSuccess { weatherData -> presentWeatherData(weatherData = weatherData) }
            .onFailure { throwable -> presentError(throwable = throwable) }
    }

    private fun presentWeatherData(weatherData: WeatherData) {
        state.update { it.copy(isLoading = false, weatherData = weatherData) }
    }

    private fun presentError(throwable: Throwable) {
        val errorMessageId = errorMapper.map(throwable = throwable)
        state.update { it.copy(isLoading = false, messageId = errorMessageId) }
    }

    fun loadForecastForMyLocation() {
        viewModelScope.launch(dispatcherProvider.io) {
            state.update { it.copy(isLoading = true) }
            locationRepository.getCurrentLocation()
                .mapCatching { latLng ->
                    geocodingRepository.getReverseGeocoding(latLng = latLng, locale = locale)
                }
                .mapCatching { geocodingItems -> geocodingItems.getOrThrow().first() }
                .onSuccess { geocodingItem -> eventAggregator.setSelectedGeocodingItem(geocodingItem) }
                .onFailure { throwable -> presentError(throwable = throwable) }
        }
    }

    fun onRetryClick() {
        state.update { it.copy(messageId = null) }
        val selectedGeocodingItem = eventAggregator.selectedGeocodingItem.replayCache.firstOrNull()
        viewModelScope.launch(dispatcherProvider.io) {
            if (selectedGeocodingItem != null) {
                eventAggregator.setSelectedGeocodingItem(geocodingItem = selectedGeocodingItem)
            } else {
                loadForecastForMyLocation()
            }
        }
    }

    fun showLocationPermissionRationale(show: Boolean) {
        state.update { it.copy(showLocationPermissionRationale = show) }
    }
}