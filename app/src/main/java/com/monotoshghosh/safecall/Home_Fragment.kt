package com.monotoshghosh.safecall

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.monotoshghosh.safecall.databinding.HomeFragmentBinding
import java.util.Locale

class Home_Fragment : Fragment(R.layout.home_fragment) {

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!
    private val TAG = "Home_Fragment"
    private val GALLERY_REQUEST_CODE = 1000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MobileAds.initialize(requireContext()) {}

        val adRequest = AdRequest.Builder().build()
//        binding.adView1.loadAd(adRequest)


        binding.sirenGif.alpha = 0f

        // ENABLING THE GIF
        val sirenGifImg = view.findViewById<ImageView>(R.id.sirenGif)
        Glide.with(this).asGif().load(R.drawable.sirengif).into(sirenGifImg)

        binding.btnHomeFragment.setOnClickListener {
            val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!isLocationEnabled(locationManager)) {
                objSound.btnSoundError(requireActivity())
                objVibration.vibrate(requireContext())
                Toast.makeText(requireContext(), "Please turn ON your Location", Toast.LENGTH_LONG).show()
            }

            val adminName = getAdminName()
            val locationService = LocationService(requireContext())

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
            } else {
                locationService.getCurrentLocation { location ->
                    val message: String
                    if (location != null) {
                        val googleMapsLink = getGoogleMapsLink(location)
                        val placeName = getPlaceName(location)
                        message = "ALERT!!!\n$adminName is in Danger.\nKindly contact urgently.\nCurrent location: $placeName.\nYou can also view the location on Google Maps here:\n$googleMapsLink"
                    } else {
                        message = "ALERT!!!\n$adminName is in Danger.\nKindly contact urgently.\nCurrent location is unknown."
                    }

                    Log.d(TAG, "Generated message: $message")
                    val phoneNumbers = getSavedPersonNumbers()
                    if (phoneNumbers.isNotEmpty()) {
                        phoneNumbers.forEach { phoneNumber ->
                            sendSms(phoneNumber, message)
                        }
                    } else {
                        Toast.makeText(requireContext(), "No phone numbers saved", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }




    private fun getAdminName(): String {
        val sharedPreferences = requireContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        return sharedPreferences.getString("Name", "Admin") ?: "Admin"
    }

    private fun getSavedPersonNumbers(): List<String> {
        val sharedPreferences = requireContext().getSharedPreferences("PeopleInfo", Context.MODE_PRIVATE)
        val phoneNumbers = mutableListOf<String>()
        for (i in 1..6) {
            val phoneNumber = sharedPreferences.getString("Person${i}_Phone", "")
            if (!phoneNumber.isNullOrEmpty()) {
                phoneNumbers.add(phoneNumber)
            }
        }
        return phoneNumbers
    }

    private fun sendSms(phoneNumber: String, message: String) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.SEND_SMS), 102)
        } else {
            try {
                val smsManager = SmsManager.getDefault()
                val parts = smsManager.divideMessage(message)
                smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null)
                Toast.makeText(requireContext(), "SMS sent to $phoneNumber", Toast.LENGTH_SHORT).show()
                objSound.btnSoundStart(requireActivity())
                Log.d(TAG, "SMS sent to $phoneNumber")
                allowStartBtnAndGif()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Failed to send SMS: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Failed to send SMS to $phoneNumber: ${e.message}")
            }
        }
    }

//    fun allowStartBtnAndGif() {
//        // BUTTON BACKGROUND CHANGE and SIREN ANIMATION
//        binding.btnHomeFragment.background = ContextCompat.getDrawable(requireContext(),
//            R.drawable.button_pressed
//        )
//        binding.sirenGif.alpha = 1f  // SHOW IMAGE
//
//        Handler().postDelayed({
//            binding.btnHomeFragment.background = ContextCompat.getDrawable(requireContext(),
//                R.drawable.button_not_pressed
//            )
//            binding.sirenGif.alpha = 0f
//        }, 7900)
//    }

    fun allowStartBtnAndGif() {
        // Added a null check to ensure `_binding` is not null before accessing it
        if (_binding == null) {
            Log.e(TAG, "Binding is null in allowStartBtnAndGif") // Log error if `_binding` is null
            return
        }

        // Button background change and siren animation
        binding.btnHomeFragment.background = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.button_pressed
        )
        binding.sirenGif.alpha = 1f  // Show image

        Handler().postDelayed({
            // Added an additional null check here to prevent accessing `_binding` after fragment is destroyed
            if (_binding != null) {
                binding.btnHomeFragment.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.button_not_pressed
                )
                binding.sirenGif.alpha = 0f
            } else {
                Log.e(TAG, "Binding is null in postDelayed callback") // Log error if `_binding` becomes null
            }
        }, 7900)
    }




    private fun getGoogleMapsLink(location: Location): String {
        return "https://www.google.com/maps/search/?api=1&query=${location.latitude},${location.longitude}"
    }

    private fun getPlaceName(location: Location): String {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        return if (!addresses.isNullOrEmpty()) {
            addresses[0].getAddressLine(0)
        } else {
            "an unknown place"
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            101 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission granted for location
                    Toast.makeText(requireContext(), "Permission granted for location", Toast.LENGTH_SHORT).show()
                    binding.btnHomeFragment.performClick()
                } else {
                    // Permission denied for location
                    Toast.makeText(requireContext(), "Permission denied for location", Toast.LENGTH_SHORT).show()
                }
            }
            102 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission granted for sending SMS
                    Toast.makeText(requireContext(), "Permission granted for sending SMS", Toast.LENGTH_SHORT).show()
                    binding.btnHomeFragment.performClick()
                } else {
                    // Permission denied for sending SMS
                    Toast.makeText(requireContext(), "Permission denied for sending SMS", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun isLocationEnabled(locationManager: LocationManager): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}