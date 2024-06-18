package com.example.seniorcare

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.seniorcare.databinding.ProfileFragmentBinding

class Profile_Fragment: Fragment(R.layout.profile_fragment), OnProfileUpdatedListener {

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

        loadProfileData()

        binding.editBtnprofileFragment.setOnClickListener {
//            Toast.makeText(requireContext(), "Edit Box Opening", Toast.LENGTH_SHORT).show()
            obj().newRegistrationDialogBox(requireContext(),null,this) // here i am updating the details but not immdediately it is reflected when app reopen or fragmnt switch
//            objSound.btnSound(requireActivity())

        }


    }

    private fun loadProfileData(){
        // RETRIEVE THE STORED DATA AND DISPLAY IT
        val sharedPreferences = requireContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE)

        val name = sharedPreferences.getString("Name", "Not Set")
        val age = sharedPreferences.getString("Age", "Not Set")
        val bloodGroup = sharedPreferences.getString("Blood Group", "Not Set")
        val location = sharedPreferences.getString("Location", "Not Set")
        val phone = sharedPreferences.getString("Phone", "Not Set")


        binding.proName.text = "$name"
        binding.proAge.text = "$age"
        binding.proBloodGroup.text = "$bloodGroup"
        binding.proLocation.text = "$location"
        binding.proPhoneNo.text = "$phone"

    }

    override fun onProfileUpdated() {
        loadProfileData()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}