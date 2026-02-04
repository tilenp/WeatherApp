package com.example.weatherapp.screens.forecast.viewmodel

import com.example.domain.model.GeocodingItem
import com.example.domain.model.LatLng
import com.example.domain.model.Units
import com.example.domain.model.WeatherData
import com.example.domain.repository.GeocodingRepository
import com.example.domain.repository.LocationRepository
import com.example.domain.repository.WeatherRepository
import com.example.weatherapp.R
import com.example.weatherapp.screens.EventAggregator
import com.example.weatherapp.screens.forecast.mapper.ForecastStateUiMapper
import com.example.weatherapp.screens.forecast.model.ForecastStateUi
import com.example.weatherapp.utils.ErrorMapper
import com.example.weatherapp.utils.TestDispatcherProvider
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertTrue
import java.io.IOException
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
internal class ForecastViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val dispatcherProvider = TestDispatcherProvider(testDispatcher)

    private val eventAggregator = EventAggregator()
    private val geocodingRepository: GeocodingRepository = mockk()
    private val locationRepository: LocationRepository = mockk()
    private val weatherRepository: WeatherRepository = mockk()
    private val errorMapper: ErrorMapper = mockk()
    private val uiMapper: ForecastStateUiMapper = mockk()

    private lateinit var viewModel: ForecastViewModel

    private val locale = Locale.UK

    @BeforeEach
    fun setup() {
        every { uiMapper.map(any(), any()) } returns ForecastStateUi.Loading
        every { errorMapper.map(any()) } returns R.string.unknown_error

        viewModel = ForecastViewModel(
            eventAggregator = eventAggregator,
            geocodingRepository = geocodingRepository,
            locationRepository = locationRepository,
            weatherRepository = weatherRepository,
            forecastStateUiMapper = uiMapper,
            errorMapper = errorMapper,
            locale = locale,
            dispatcherProvider = dispatcherProvider
        )
    }

    @Test
    fun `initial state is Loading`() = runTest {
        val state = viewModel.stateUi.first()

        assertTrue(state is ForecastStateUi.Loading)
    }

    @Test
    fun `selecting geocoding item loads weather data`() = runTest {
        val latLng = LatLng(51.5, -0.1)
        val geocodingItem = GeocodingItem(
            name = "London",
            latLng = latLng,
            country = "GB",
            state = null
        )

        val weatherData = WeatherData(
            current = null,
            daily = emptyList()
        )

        coEvery {
            weatherRepository.getWeatherData(latLng, Units.metric, locale)
        } returns Result.success(weatherData)

        eventAggregator.setSelectedGeocodingItem(geocodingItem)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 1) {
            weatherRepository.getWeatherData(latLng, Units.metric, locale)
        }
    }

    @Test
    fun `weather repository failure shows error`() = runTest {
        val latLng = LatLng(1.0, 2.0)
        val geocodingItem = GeocodingItem(
            name = "Test",
            latLng = latLng,
            country = "X",
            state = null
        )

        val error = IOException("Network")
        every { errorMapper.map(error) } returns R.string.network_error

        coEvery {
            weatherRepository.getWeatherData(any(), any(), any())
        } returns Result.failure(error)

        eventAggregator.setSelectedGeocodingItem(geocodingItem)
        testDispatcher.scheduler.advanceUntilIdle()

        verify {
            errorMapper.map(error)
        }
    }

    @Test
    fun `loadForecastForMyLocation success triggers reverse geocoding`() = runTest {
        val latLng = LatLng(10.0, 20.0)
        val geocodingItem = GeocodingItem(
            name = "Paris",
            latLng = latLng,
            country = "FR",
            state = null
        )

        coEvery {
            locationRepository.getCurrentLocation()
        } returns Result.success(latLng)

        coEvery {
            geocodingRepository.getReverseGeocoding(latLng, locale)
        } returns Result.success(listOf(geocodingItem))

        coEvery {
            weatherRepository.getWeatherData(latLng, Units.metric, locale)
        } returns Result.success(WeatherData(current = null, daily = emptyList()))

        viewModel.loadForecastForMyLocation()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerifyOrder {
            locationRepository.getCurrentLocation()
            geocodingRepository.getReverseGeocoding(latLng, locale)
            weatherRepository.getWeatherData(latLng, Units.metric, locale)
        }
    }

    @Test
    fun `loadForecastForMyLocation failure shows error`() = runTest {
        val error = IOException("No GPS")
        every { errorMapper.map(error) } returns R.string.network_error

        coEvery {
            locationRepository.getCurrentLocation()
        } returns Result.failure(error)

        viewModel.loadForecastForMyLocation()
        testDispatcher.scheduler.advanceUntilIdle()

        verify {
            errorMapper.map(error)
        }
    }

    @Test
    fun `onReloadClick reloads selected city if exists`() = runTest {
        val latLng = LatLng(1.0, 1.0)
        val geocodingItem = GeocodingItem(
            name = "Berlin",
            latLng = latLng,
            country = "DE",
            state = null
        )

        coEvery {
            weatherRepository.getWeatherData(any(), any(), any())
        } returns Result.success(WeatherData(current = null, daily = emptyList()))

        eventAggregator.setSelectedGeocodingItem(geocodingItem)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onReloadClick()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(atLeast = 2) {
            weatherRepository.getWeatherData(latLng, Units.metric, locale)
        }
    }
}
