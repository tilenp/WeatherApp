package com.example.weatherapp.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun rememberLocationPermissionLauncher(
    onPermissionGranted: () -> Unit,
): ManagedActivityResultLauncher<String, Boolean> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPermissionGranted()
        }
    }
}

fun handleLocationPermission(
    context: Context,
    activity: Activity,
    launcher: ManagedActivityResultLauncher<String, Boolean>,
    showLocationPermissionRationale: (Boolean) -> Unit,
    onGranted: () -> Unit
) {
    when {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED -> {
            // Permission already granted
            onGranted()
        }

        ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) -> {
            // User denied once → show rationale
            showLocationPermissionRationale(true)
        }

        else -> {
            // First time OR permanently denied
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}
