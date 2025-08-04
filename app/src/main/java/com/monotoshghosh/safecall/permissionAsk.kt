package com.monotoshghosh.safecall

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object permissionAsk {

    private const val SMS_PERMISSION_CODE = 101
    private const val LOCATION_PERMISSION_CODE = 102

    // Requests SMS permission if not already granted
    fun checkAndRequestSmsPermission(context: Context) {
        val activity = context as Activity
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Show rationale if needed, otherwise directly request
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.SEND_SMS
                )
            ) {
                showExplanationDialog(
                    context,
                    "SMS Permission Needed",
                    "This app needs SMS permission to send emergency alerts.",
                    Manifest.permission.SEND_SMS,
                    SMS_PERMISSION_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.SEND_SMS),
                    SMS_PERMISSION_CODE
                )
            }
        }
    }

    // Requests fine location permission
    fun checkAndRequestLocationPermission(context: Context) {
        val activity = context as Activity
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_CODE
            )
        }
    }

    // Helper function to check SMS permission first
    fun checkAndRequestPermissions(context: Context) {
        checkAndRequestSmsPermission(context)
    }


    // Handles the result of permission requests
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
        context: Context
    ) {
        val activity = context as Activity
        when (requestCode) {
            SMS_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // If SMS granted, now request Location
                    checkAndRequestLocationPermission(context)
                } else {
                    val permanentlyDenied = !ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        Manifest.permission.SEND_SMS
                    )

                    if (permanentlyDenied) {
                        // Show dialog to redirect to settings
                        showSettingsRedirectDialog(
                            context,
                            "Permission Denied",
                            "You have permanently denied SMS permission. Please enable it manually from App Settings."
                        )
                    } else {
                        // Show toast for manufacturers like Vivo, Xiaomi, etc.
                        showROMWarningToast(context)
                    }
                }
            }

            LOCATION_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Location permission granted — nothing to do here
                }
            }
        }
    }

    // Shows a dialog explaining why a permission is needed before requesting it again
    private fun showExplanationDialog(
        context: Context,
        title: String,
        message: String,
        permission: String,
        requestCode: Int
    ) {
        val activity = context as Activity
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Allow") { _, _ ->
                ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Shows a dialog redirecting user to App Settings for manually enabling permissions
    private fun showSettingsRedirectDialog(context: Context, title: String, message: String) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Go to Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Shows warning toast for ROMs that often block permissions even when granted
    private fun showROMWarningToast(context: Context) {
        val manufacturer = Build.MANUFACTURER.lowercase()
        if (manufacturer.contains("vivo") || manufacturer.contains("xiaomi") ||
            manufacturer.contains("realme") || manufacturer.contains("oppo")
        ) {
            Toast.makeText(
                context,
                "SMS permission may still be blocked by system settings. Please go to App Settings > Permissions and manually enable SMS.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
