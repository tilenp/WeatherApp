package com.example.data.mapper

import com.example.data.model.CurrentWeatherDto
import com.example.data.model.DailyWeatherDto
import com.example.data.model.TemperatureDto
import com.example.data.model.WeatherDataDto
import com.example.data.model.WeatherDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class WeatherDataMapperTest {

    private val mapper = WeatherDataMapper()

    @Test
    fun `map returns WeatherData with null current when current dto is null`() {
        val dto = WeatherDataDto(
            current = null,
            daily = null
        )

        val result = mapper.map(dto)

        assertNull(result.current)
        assertTrue(result.daily.isEmpty())
    }

    @Test
    fun `mapCurrent returns null when dt is missing`() {
        val dto = WeatherDataDto(
            current = CurrentWeatherDto(
                dt = null,
                temp = 20.0,
                feelsLike = 18.0,
                pressure = 1012,
                humidity = 70,
                windSpeed = 5.0,
                weather = null
            ),
            daily = null
        )

        val result = mapper.map(dto)

        assertNull(result.current)
    }

    @Test
    fun `map maps current weather correctly`() {
        val dto = WeatherDataDto(
            current = CurrentWeatherDto(
                dt = 1_700_000_000,
                temp = 22.5,
                feelsLike = 21.0,
                pressure = 1015,
                humidity = 60,
                windSpeed = 4.5,
                weather = listOf(
                    WeatherDto(
                        id = 800,
                        description = "clear sky",
                        icon = "01d"
                    )
                )
            ),
            daily = null
        )

        val result = mapper.map(dto)
        val current = result.current!!

        assertEquals(1_700_000_000, current.dt)
        assertEquals(22.5, current.temp)
        assertEquals(21.0, current.feelsLike)
        assertEquals(1015, current.pressure)
        assertEquals(60, current.humidity)
        assertEquals(4.5, current.windSpeed)
        assertEquals(1, current.weather.size)
        assertEquals(
            "https://openweathermap.org/payload/api/media/file/01d.png",
            current.weather.first().iconUrl
        )
    }

    @Test
    fun `weather items with null id are filtered out`() {
        val dto = WeatherDataDto(
            current = CurrentWeatherDto(
                dt = 123L,
                temp = null,
                feelsLike = null,
                pressure = null,
                humidity = null,
                windSpeed = null,
                weather = listOf(
                    WeatherDto(id = null, description = "invalid", icon = "01d"),
                    WeatherDto(id = 500, description = "rain", icon = "10d")
                )
            ),
            daily = null
        )

        val result = mapper.map(dto)
        val weather = result.current!!.weather

        assertEquals(1, weather.size)
        assertEquals(500, weather.first().id)
    }

    @Test
    fun `daily items without dt are filtered out`() {
        val dto = WeatherDataDto(
            current = null,
            daily = listOf(
                DailyWeatherDto(
                    dt = null,
                    temp = null,
                    weather = null
                ),
                DailyWeatherDto(
                    dt = 1_700_000_100,
                    temp = TemperatureDto(min = 10.0, max = 18.0),
                    weather = emptyList()
                )
            )
        )

        val result = mapper.map(dto)

        assertEquals(1, result.daily.size)
        assertEquals(1_700_000_100, result.daily.first().dt)
    }

    @Test
    fun `mapDaily maps temperature correctly`() {
        val dto = WeatherDataDto(
            current = null,
            daily = listOf(
                DailyWeatherDto(
                    dt = 1234L,
                    temp = TemperatureDto(
                        min = 5.0,
                        max = 15.0
                    ),
                    weather = null
                )
            )
        )

        val result = mapper.map(dto)
        val temperature = result.daily.first().temp!!

        assertEquals(5.0, temperature.min)
        assertEquals(15.0, temperature.max)
    }

    @Test
    fun `buildIconUrl returns null when icon is null`() {
        val dto = WeatherDataDto(
            current = CurrentWeatherDto(
                dt = 123L,
                temp = null,
                feelsLike = null,
                pressure = null,
                humidity = null,
                windSpeed = null,
                weather = listOf(
                    WeatherDto(id = 800, description = "clear", icon = null)
                )
            ),
            daily = null
        )

        val result = mapper.map(dto)
        val weather = result.current!!.weather.first()

        assertNull(weather.iconUrl)
    }
}
