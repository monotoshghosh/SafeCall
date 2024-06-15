package com.example.seniorcare

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.seniorcare.databinding.ProfileFragmentBinding

class Profile_Fragment: Fragment(R.layout.profile_fragment) {

    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RETRIEVE THE STORED DATA AND DISPLAY IT
        val sharedPreferences = requireContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE)

        val name = sharedPreferences.getString("Name", "Not Set")
        val age = sharedPreferences.getString("Age", "Not Set")
        val bloodGroup = sharedPreferences.getString("Blood Group", "Not Set")
        val location = sharedPreferences.getString("Location", "Not Set")
        val phone = sharedPreferences.getString("Phone", "Not Set")


        binding.proName.text = "Name: $name"
        binding.proAge.text = "Age: $age"
        binding.proBloodGroup.text = "Blood Group: $bloodGroup"
        binding.proLocation.text = "Location: $location"
        binding.proPhoneNo.text = "Phone Number: $phone"



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}