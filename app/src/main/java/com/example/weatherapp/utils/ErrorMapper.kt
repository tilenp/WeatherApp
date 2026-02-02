package com.example.weatherapp.utils

import com.example.weatherapp.R
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ErrorMapper @Inject constructor() {

    fun map(throwable: Throwable): Int {
        return when (throwable) {
            is HttpException -> mapHttpException(throwable)
            is IOException -> R.string.network_error
            else -> R.string.unknown_error
        }
    }

    private fun mapHttpException(exception: HttpException): Int {
        return when (exception.code()) {
            401, 403 -> R.string.api_key_error
            else -> R.string.server_error
        }
    }
}
