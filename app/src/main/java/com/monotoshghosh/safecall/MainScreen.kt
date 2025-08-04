package com.monotoshghosh.safecall

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.monotoshghosh.safecall.databinding.ActivityMainScreenBinding

class MainScreen : AppCompatActivity() {
    private lateinit var binding: ActivityMainScreenBinding

    private var isAppOpenedFirstTime = true
    private val SMS_PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)

        // For API 35
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContentView(binding.root)

        // Status bar styling
        window.apply {
            statusBarColor = ContextCompat.getColor(this@MainScreen, android.R.color.white)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

//        checkAndPromptLocation(this, this) // This line triggers the location check


        // Check SMS permission on every app open
        checkSmsPermission()

        // Check and prompt for location
//        checkAndPromptLocation()

        // Check if user profile is saved
        if (!obj().isUserInfoSaved(this)) {
            obj().newRegistrationDialogBox(this)
        }

        // Check if location is enabled
        if (!locationCheck.isLocationEnabled(this)) {
            locationCheck.promptEnableLocation(this)
        }

        // Bottom Navigation
        binding.bnView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.itemPeopleId -> replaceWithFragment(People_Fragment())
                R.id.itemHomeId -> replaceWithFragment(Home_Fragment())
                R.id.itemProfileId -> replaceWithFragment(Profile_Fragment())
            }
            true
        }

        // Default fragment on launch
        if (savedInstanceState == null) {
            binding.bnView.selectedItemId = R.id.itemHomeId
        }
    }

    private fun replaceWithFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()

        if (isAppOpenedFirstTime) {
            isAppOpenedFirstTime = false
        } else {
            objSound.btnSound(this)
        }
    }

    // Check and request SMS permission
    private fun checkSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), SMS_PERMISSION_CODE)
        }
    }

    // Handle permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted: continue
                permissionAsk.checkAndRequestPermissions(this) // Also triggers location permission
            } else {
                val permanentlyDenied = !ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.SEND_SMS
                )
                if (permanentlyDenied) {
                    showSettingsRedirectDialog(
                        "Permission Required",
                        "You have permanently denied SMS permission. Please enable it manually from App Settings to continue."
                    )
                } else {
                    showExitDialog(
                        "Permission Required",
                        "This app requires SMS permission to work. Please allow it to continue."
                    )
                }
            }
        } else {
            // Handle other permissions (location, etc.)
            permissionAsk.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
        }
    }

    // Dialog to guide user to App Settings
    private fun showSettingsRedirectDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Go to Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
                finishAffinity()
            }
            .setNegativeButton("Exit") { _: DialogInterface, _: Int ->
                finishAffinity()
            }
            .show()
    }

    // Dialog to exit app if permission not granted
    private fun showExitDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Exit") { _: DialogInterface, _: Int ->
                finishAffinity()
            }
            .show()
    }

    private fun checkAndPromptLocation() {
        val locationManager = getSystemService(LOCATION_SERVICE) as android.location.LocationManager
        val gpsEnabled = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
        val networkEnabled = locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)

        if (!gpsEnabled && !networkEnabled) {
            AlertDialog.Builder(this)
                .setTitle("Enable Location")
                .setMessage("This app requires location services to be enabled. Please enable GPS or network-based location.")
                .setCancelable(false)
                .setPositiveButton("Enable") { _, _ ->
                    val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
                .setNegativeButton("Exit App") { _, _ ->
                    finishAffinity()
                }
                .show()
        }
    }

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return try {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) {
            false
        }
    }

    fun checkAndPromptLocation(context: Context, activity: Activity) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!isGpsEnabled && !isNetworkEnabled) {
            AlertDialog.Builder(context)
                .setTitle("Enable Location")
                .setMessage("Location services are required for SafeCall to work. Please enable GPS or Network Location.\n\nFor some phones like Vivo, Realme, Xiaomi, you may need to manually enable it.")
                .setCancelable(false)
                .setPositiveButton("Open Settings") { _, _ ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    activity.startActivity(intent)
                }
                .setNegativeButton("Exit App") { _, _ ->
                    activity.finish()
                }
                .show()
        } else {
            // Optional Toast (for testing)
            makeText(context, "Location is already enabled", LENGTH_SHORT).show()
        }
    }

}
