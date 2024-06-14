package com.example.seniorcare

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.seniorcare.databinding.HomeFragmentBinding
import android.telephony.SmsManager
import android.widget.Toast
import java.util.Locale

class Home_Fragment : Fragment(R.layout.home_fragment) {

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnHomeFragment.setOnClickListener {
            val adminName = getAdminName()
            val locationService = LocationService(requireContext())

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
            } else {
                locationService.getCurrentLocation { location ->
                    val message: String
                    if (location != null) {
                        val googleMapsLink = getGoogleMapsLink(location)
                        message = "Admin $adminName is currently at : $googleMapsLink"
                    } else {
                        message = "Admin $adminName is currently at an unknown location."
                    }

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
                smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                Toast.makeText(requireContext(), "SMS sent to $phoneNumber", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Failed to send SMS: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getGoogleMapsLink(location: Location): String {
        return "https://www.google.com/maps/search/?api=1&query=${location.latitude},${location.longitude}"
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
