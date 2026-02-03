package com.example.weatherapp.screens.forecast.view

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.weatherapp.R


@Composable
internal fun LocationPermissionRationaleDialog(
    onGrantPermission: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.location_permission_title))
        },
        text = {
            Text(text = stringResource(R.string.location_permission_rationale))
        },
        confirmButton = {
            TextButton(onClick = onGrantPermission) {
                Text(text = stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.dismiss))
            }
        }
    )
}
