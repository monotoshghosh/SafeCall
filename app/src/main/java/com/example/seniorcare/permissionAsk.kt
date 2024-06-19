package com.example.seniorcare

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object permissionAsk {

    private const val SMS_PERMISSION_CODE = 101
    private const val LOCATION_PERMISSION_CODE = 102

    // Function to check and request SMS permission
    fun checkAndRequestSmsPermission(context: Context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity,
                arrayOf(Manifest.permission.SEND_SMS),
                SMS_PERMISSION_CODE)
        }
    }

    // Function to check and request Location permission
    fun checkAndRequestLocationPermission(context: Context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_CODE)
        }
    }

    // Function to check and request both SMS and Location permissions
    fun checkAndRequestPermissions(context: Context) {
        checkAndRequestSmsPermission(context)
    }

    // Handle onRequestPermissionsResult in your activity or fragment
    fun onRequestPermissionsResult(requestCode: Int,
                                   permissions: Array<String>, grantResults: IntArray, context: Context) {
        when (requestCode) {
            SMS_PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted.
                    checkAndRequestLocationPermission(context)
                } else {
                    // Permission denied.
                }
                return
            }
            LOCATION_PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted.
                } else {
                    // Permission denied.
                }
                return
            }
            // Add more cases for other permissions you may have
        }
    }
}
