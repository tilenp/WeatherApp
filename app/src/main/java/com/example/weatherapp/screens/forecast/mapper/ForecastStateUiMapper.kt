package com.example.weatherapp.screens.forecast.mapper

import android.text.format.DateUtils
import com.example.domain.model.CurrentWeather
import com.example.domain.model.DailyWeather
import com.example.domain.model.Units
import com.example.weatherapp.screens.forecast.model.CurrentWeatherUi
import com.example.weatherapp.screens.forecast.model.DailyWeatherUi
import com.example.weatherapp.screens.forecast.model.ForecastStateData
import com.example.weatherapp.screens.forecast.model.ForecastStateUi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.math.roundToInt

internal class ForecastStateUiMapper @Inject constructor(
    locale: Locale,
) {
    private val formatter = SimpleDateFormat("EEE", locale)

    fun map(stateData: ForecastStateData, units: Units): ForecastStateUi {
        return mapLoading(stateData = stateData)
            ?: mapMessage(stateData = stateData)
            ?: mapContent(stateData = stateData, units = units)
    }

    private fun mapLoading(stateData: ForecastStateData): ForecastStateUi? {
        if (!stateData.isLoading) return null
        return ForecastStateUi.Loading
    }

    private fun mapMessage(stateData: ForecastStateData): ForecastStateUi? {
        if (stateData.messageId == null) return null
        return ForecastStateUi.Message(messageId = stateData.messageId)
    }

    private fun mapContent(stateData: ForecastStateData, units: Units): ForecastStateUi {
        val weatherData = stateData.weatherData
        return ForecastStateUi.Content(
            cityName = stateData.cityName,
            current = mapCurrent(currentWeather = weatherData?.current, units = units),
            daily = weatherData?.daily?.map { daily -> mapDaily(dailyWeather = daily, units = units) }.orEmpty(),
            showLocationPermissionRationale = stateData.showLocationPermissionRationale,
        )
    }

    private fun mapCurrent(currentWeather: CurrentWeather?, units: Units): CurrentWeatherUi {
        val weather = currentWeather?.weather?.firstOrNull()

        return CurrentWeatherUi(
            temp = formatTemp(value = currentWeather?.temp, units = units),
            feelsLike = formatTemp(value = currentWeather?.feelsLike, units = units),
            pressure = formatPressure(value = currentWeather?.pressure),
            humidity = formatHumidity(value = currentWeather?.humidity),
            windSpeed = formatWind(value = currentWeather?.windSpeed, units = units),
            iconUrl = weather?.iconUrl.orEmpty(),
            description = weather?.description.orEmpty(),
        )
    }

    private fun mapDaily(dailyWeather: DailyWeather, units: Units): DailyWeatherUi {
        val weather = dailyWeather.weather.firstOrNull()

        return DailyWeatherUi(
            day = formatDay(dailyWeather.dt),
            minTemp = formatTemp(value = dailyWeather.temp?.min, units = units),
            maxTemp = formatTemp(value = dailyWeather.temp?.max, units = units),
            iconUrl = weather?.iconUrl,
        )
    }

    private fun formatTemp(value: Double?, units: Units): String {
        val unitSymbol = when (units) {
            Units.metric -> "°C"
        }
        return value?.let { "${it.roundToInt()} $unitSymbol" } ?: "— $unitSymbol"
    }

    private fun formatPressure(value: Int?): String {
        return value?.let { "$it hPa" } ?: "— hPa"
    }

    private fun formatHumidity(value: Int?): String {
        return value?.let { "$it%" } ?: "— %"
    }

    private fun formatWind(value: Double?, units: Units): String {
        val unitSymbol = when (units) {
            Units.metric -> "m/s"
        }
        return value?.let { "${it.roundToInt()} $unitSymbol" } ?: "— $unitSymbol"
    }

    private fun formatDay(timestampSeconds: Long): String {
        val millis = timestampSeconds * 1_000
        return if (DateUtils.isToday(millis)) "Today" else formatter.format(Date(millis))
    }
}