package com.example.seniorcare

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.seniorcare.databinding.PeopleFragmentBinding

class People_Fragment : Fragment(R.layout.people_fragment) {

    private var _binding: PeopleFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PeopleFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {       //    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onResume()            //         super.onViewCreated(view, savedInstanceState)

        refreshImages()
    }

    private fun refreshImages() {
        val cardPersons = arrayOf(
            binding.person1 to binding.person1Img,
            binding.person2 to binding.person2Img,
            binding.person3 to binding.person3Img,
            binding.person4 to binding.person4Img,
            binding.person5 to binding.person5Img,
            binding.person6 to binding.person6Img
        )

        for ((card, imageView) in cardPersons) {
            val personKey = when (card.id) {
                R.id.person1 -> "Person1"
                R.id.person2 -> "Person2"
                R.id.person3 -> "Person3"
                R.id.person4 -> "Person4"
                R.id.person5 -> "Person5"
                R.id.person6 -> "Person6"
                else -> ""
            }

            if (personKey.isNotEmpty()) {
                val savedImageUri = getSavedImageUri(personKey)
                if (savedImageUri != null) {
                    imageView.setImageURI(savedImageUri)
                } else {
                    imageView.setImageResource(R.drawable.addphoto3) // default image
                }

                card.setOnClickListener {
                    if (!isPersonInfoSaved(personKey, it.context)) {
                        obj().newRegistrationDialogBox(it.context, personKey)
                    } else {
                        Toast.makeText(requireContext(), "Person Already Saved !!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireContext(), savedPersonInfo::class.java)
                        intent.putExtra("Person_unique_key", personKey)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun getSavedImageUri(personKey: String): Uri? {
        val sharedPreferences = requireContext().getSharedPreferences("PeopleInfoPhoto", Context.MODE_PRIVATE)
        val savedImageUriString = sharedPreferences.getString("${personKey}_Photo", null)
        return if (savedImageUriString != null) Uri.parse(savedImageUriString) else null
    }

    private fun isPersonInfoSaved(personKey: String, context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("PeopleInfo", Context.MODE_PRIVATE)
        return sharedPreferences.contains("${personKey}_Name") &&
                sharedPreferences.contains("${personKey}_Age") &&
                sharedPreferences.contains("${personKey}_BloodGroup") &&
                sharedPreferences.contains("${personKey}_Location") &&
                sharedPreferences.contains("${personKey}_Phone")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
