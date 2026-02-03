package com.example.data.repository

import androidx.annotation.RequiresPermission
import com.example.domain.model.LatLng
import com.example.domain.repository.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

internal class LocationRepositoryImpl @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient
) : LocationRepository {

    @RequiresPermission(
        allOf = [
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
        ]
    )
    override suspend fun getCurrentLocation(): Result<LatLng> = suspendCancellableCoroutine { continuation ->
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            null
        )
            .addOnSuccessListener { location ->
                continuation.resume(
                    value = Result.success(
                        LatLng(lat = location.latitude, lon = location.longitude)
                    ),
                    onCancellation = { throwable, _, _ ->
                        Result.failure<LatLng>(throwable)
                    }
                )
            }
            .addOnFailureListener { throwable ->
                continuation.resume(
                    value = Result.failure(throwable),
                    onCancellation = { throwable, _, _ ->
                        Result.failure<LatLng>(throwable)
                    }
                )
            }
    }
}