package com.example.data.mapper

import com.example.data.model.GeocodingItemDto
import com.example.domain.model.GeocodingItem
import com.example.domain.model.LatLng
import java.util.Locale
import javax.inject.Inject

internal class GeocodingItemMapper @Inject constructor() {

    fun map(dtos: List<GeocodingItemDto>?, locale: Locale): List<GeocodingItem> {
        return dtos?.mapNotNull { mapItem(dto = it, locale = locale) }.orEmpty()
    }

    private fun mapItem(dto: GeocodingItemDto?, locale: Locale): GeocodingItem? {
        if (dto == null) return null
        val lat = dto.lat ?: return null
        val lon = dto.lon ?: return null
        val name = resolveName(dto = dto, locale = locale) ?: return null
        val country = dto.country ?: return null

        return GeocodingItem(
            name = name,
            latLng = LatLng(lat = lat, lon = lon),
            country = country,
            state = dto.state,
        )
    }

    private fun resolveName(dto: GeocodingItemDto, locale: Locale): String? {
        return dto.localNames
            ?.get(locale.language)
            ?.takeIf { it.isNotBlank() }
            ?: dto.name
    }
}
