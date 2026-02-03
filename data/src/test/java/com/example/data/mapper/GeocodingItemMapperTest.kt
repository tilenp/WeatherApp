package com.example.data.mapper

import com.example.data.model.GeocodingItemDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.Locale

internal class GeocodingItemMapperTest {

    private val mapper = GeocodingItemMapper()
    private val locale = Locale.forLanguageTag("en-US")

    @Test
    fun `map returns empty list when dtos is null`() {
        val result = mapper.map(null, locale)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `map filters out items with missing required fields`() {
        val dtos = listOf(
            GeocodingItemDto(
                name = "Berlin",
                localNames = null,
                lat = null, // invalid
                lon = 13.4050,
                country = "DE",
                state = null
            ),
            GeocodingItemDto(
                name = "Paris",
                localNames = null,
                lat = 48.8566,
                lon = 2.3522,
                country = "FR",
                state = null
            )
        )

        val result = mapper.map(dtos, locale)

        assertEquals(1, result.size)
        assertEquals("Paris", result.first().name)
    }

    @Test
    fun `map uses localized name when available and not blank`() {
        val dtos = listOf(
            GeocodingItemDto(
                name = "Munich",
                localNames = mapOf(
                    "en" to "Munich",
                    "de" to "München"
                ),
                lat = 48.1351,
                lon = 11.5820,
                country = "DE",
                state = "Bavaria"
            )
        )

        val result = mapper.map(dtos, Locale.forLanguageTag("de-DE"))

        assertEquals(1, result.size)
        assertEquals("München", result.first().name)
    }

    @Test
    fun `map falls back to default name when localized name is blank`() {
        val dtos = listOf(
            GeocodingItemDto(
                name = "Rome",
                localNames = mapOf("it-IT" to " "),
                lat = 41.9028,
                lon = 12.4964,
                country = "IT",
                state = null
            )
        )

        val result = mapper.map(dtos, Locale.forLanguageTag("it-IT"))

        assertEquals("Rome", result.first().name)
    }

    @Test
    fun `map correctly maps all fields`() {
        val dto = GeocodingItemDto(
            name = "Madrid",
            localNames = null,
            lat = 40.4168,
            lon = -3.7038,
            country = "ES",
            state = "Community of Madrid"
        )

        val result = mapper.map(listOf(dto), locale).first()

        assertEquals("Madrid", result.name)
        assertEquals(40.4168, result.latLng.lat)
        assertEquals(-3.7038, result.latLng.lon)
        assertEquals("ES", result.country)
        assertEquals("Community of Madrid", result.state)
    }
}
