package com.example.seniorcare

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.seniorcare.databinding.PeopleFragmentBinding

class People_Fragment:Fragment(R.layout.people_fragment) {

    private var _binding: PeopleFragmentBinding? = null
    private val binding get() = _binding!!

    private val obj = obj()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PeopleFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var cardPersons = arrayOf<CardView>(binding.person1,binding.person2,binding.person3,binding.person4,binding.person5,binding.person6)

        for (i in cardPersons) {
            i.setOnClickListener {
                val personKey = when (i.id) {
                    R.id.person1 -> "Person1"
                    R.id.person2 -> "Person2"
                    R.id.person3 -> "Person3"
                    R.id.person4 -> "Person4"
                    R.id.person5 -> "Person5"
                    R.id.person6 -> "Person6"
                    else -> ""
                }

                if (personKey.isNotEmpty() && !isPersonInfoSaved(personKey, it.context)) {
                    obj.newRegistrationDialogBox(it.context, personKey)
                }
                else{
                    Toast.makeText(requireContext(), "Person Already Saved !!", Toast.LENGTH_SHORT).show()
                }
            }
        }
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