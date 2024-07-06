package com.monotoshghosh.safecall

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings

object locationCheck {

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun promptEnableLocation(activity: Activity) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Enable Location")
        builder.setMessage("Your locations setting is set to 'Off'.\nPlease enable location to use this app")
        builder.setPositiveButton("Location Settings") { dialogInterface: DialogInterface, i: Int ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            activity.startActivity(intent)
        }
        builder.setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }


}