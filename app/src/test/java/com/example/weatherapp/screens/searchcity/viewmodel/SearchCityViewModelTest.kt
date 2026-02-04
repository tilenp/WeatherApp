package com.example.weatherapp.screens.searchcity.viewmodel

import app.cash.turbine.test
import androidx.compose.ui.text.input.TextFieldValue
import com.example.domain.model.GeocodingItem
import com.example.domain.model.LatLng
import com.example.domain.repository.GeocodingRepository
import com.example.weatherapp.R
import com.example.weatherapp.screens.EventAggregator
import com.example.weatherapp.screens.searchcity.mapper.SearchCityStateUiMapper
import com.example.weatherapp.screens.searchcity.model.SearchCityStateUi
import com.example.weatherapp.utils.ErrorMapper
import com.example.weatherapp.utils.TestDispatcherProvider
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.IOException
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class SearchCityViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val dispatcherProvider = TestDispatcherProvider(testDispatcher)

    private val eventAggregator = EventAggregator()
    private val geocodingRepository: GeocodingRepository = mockk()
    private val errorMapper: ErrorMapper = mockk()
    private val uiMapper = SearchCityStateUiMapper()

    private lateinit var viewModel: SearchCityViewModel

    private val locale = Locale.UK

    @BeforeEach
    fun setup() {
        every { errorMapper.map(any()) } returns R.string.unknown_error

        viewModel = SearchCityViewModel(
            eventAggregator = eventAggregator,
            geocodingRepository = geocodingRepository,
            searchCityStateUiMapper = uiMapper,
            errorMapper = errorMapper,
            locale = locale,
            dispatcherProvider = dispatcherProvider
        )
    }

    @Test
    fun `search cities happy flow`() = runTest {
        val geocodingItems = listOf(
            GeocodingItem(
                name = "London",
                latLng = LatLng(51.5, -0.1),
                country = "GB",
                state = null
            )
        )

        coEvery {
            geocodingRepository.getDirectGeocoding("London", locale)
        } returns Result.success(geocodingItems)

        viewModel.stateUi.test {
            assertTrue(awaitItem() is SearchCityStateUi.Loading)

            viewModel.onSearchInputChange(TextFieldValue("London"))
            testDispatcher.scheduler.advanceUntilIdle()
            val content1 = awaitItem() as SearchCityStateUi.Content
            assertEquals("London", content1.searchInput.text)

            viewModel.onSearchClick()
            testDispatcher.scheduler.advanceUntilIdle()

            val content2 = awaitItem() as SearchCityStateUi.Content
            assertEquals(geocodingItems, content2.geocodingItems)
        }
    }

    @Test
    fun `search cities error flow`() = runTest {
        val error = IOException("Network")
        every { errorMapper.map(error) } returns R.string.network_error

        coEvery {
            geocodingRepository.getDirectGeocoding(any(), any())
        } returns Result.failure(error)

        viewModel.stateUi.test {
            assertTrue(awaitItem() is SearchCityStateUi.Loading)

            viewModel.onSearchInputChange(TextFieldValue("London"))
            testDispatcher.scheduler.advanceUntilIdle()
            val content1 = awaitItem() as SearchCityStateUi.Content
            assertEquals("London", content1.searchInput.text)

            viewModel.onSearchClick()
            testDispatcher.scheduler.advanceUntilIdle()

            val message = awaitItem() as SearchCityStateUi.Message
            assertEquals(R.string.network_error, message.messageId)
        }
    }

    @Test
    fun `onGeocodingItemSelected emits item to EventAggregator`() = runTest {
        val geocodingItem = GeocodingItem(
            name = "Berlin",
            latLng = LatLng(52.52, 13.405),
            country = "DE",
            state = null
        )

        viewModel.onGeocodingItemSelected(geocodingItem)
        testDispatcher.scheduler.advanceUntilIdle()

        val emitted = eventAggregator.selectedGeocodingItem.first()
        assertEquals(geocodingItem, emitted)
    }
}




