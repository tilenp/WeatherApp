package com.example.weatherapp.screens.forecast.mapper

import com.example.domain.model.CurrentWeather
import com.example.domain.model.Units
import com.example.domain.model.Weather
import com.example.domain.model.WeatherData
import com.example.weatherapp.screens.forecast.model.ForecastStateData
import com.example.weatherapp.screens.forecast.model.ForecastStateUi
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.Locale

internal class ForecastStateUiMapperTest {

    private val locale = Locale.UK
    private val mapper = ForecastStateUiMapper(locale)
    private val units = Units.metric

    @Test
    fun `map returns Loading when isLoading is true`() {
        val state = ForecastStateData(
            isLoading = true,
            messageId = 123,
            weatherData = null
        )

        val result = mapper.map(state, units)

        assertTrue(result is ForecastStateUi.Loading)
    }

    @Test
    fun `map returns Message when messageId is present and not loading`() {
        val state = ForecastStateData(
            isLoading = false,
            messageId = 42
        )

        val result = mapper.map(state, units)

        assertTrue(result is ForecastStateUi.Message)
        assertEquals(42, (result as ForecastStateUi.Message).messageId)
    }

    @Test
    fun `mapCurrent formats values correctly`() {
        val weatherData = WeatherData(
            current = CurrentWeather(
                dt = 1L,
                temp = 21.6,
                feelsLike = 20.2,
                pressure = 1013,
                humidity = 55,
                windSpeed = 4.7,
                weather = listOf(
                    Weather(
                        id = 800,
                        description = "Clear",
                        iconUrl = "icon.png"
                    )
                )
            ),
            daily = emptyList()
        )

        val state = ForecastStateData(
            weatherData = weatherData
        )

        val result = mapper.map(state, units) as ForecastStateUi.Content
        val current = result.current

        assertEquals("22 °C", current.temp)
        assertEquals("20 °C", current.feelsLike)
        assertEquals("1013 hPa", current.pressure)
        assertEquals("55%", current.humidity)
        assertEquals("5 m/s", current.windSpeed)
        assertEquals("icon.png", current.iconUrl)
        assertEquals("Clear", current.description)
    }

    @Test
    fun `mapCurrent uses placeholders when values are null`() {
        val weatherData = WeatherData(
            current = null,
            daily = emptyList()
        )

        val state = ForecastStateData(weatherData = weatherData)

        val result = mapper.map(state, units) as ForecastStateUi.Content
        val current = result.current

        assertEquals("— °C", current.temp)
        assertEquals("— °C", current.feelsLike)
        assertEquals("— hPa", current.pressure)
        assertEquals("— %", current.humidity)
        assertEquals("— m/s", current.windSpeed)
        assertEquals("", current.iconUrl)
        assertEquals("", current.description)
    }

    @Test
    fun `showLocationPermissionRationale is propagated`() {
        val state = ForecastStateData(
            showLocationPermissionRationale = true,
            weatherData = WeatherData(
                current = null,
                daily = emptyList()
            )
        )

        val result = mapper.map(state, units) as ForecastStateUi.Content

        assertTrue(result.showLocationPermissionRationale)
    }
}
