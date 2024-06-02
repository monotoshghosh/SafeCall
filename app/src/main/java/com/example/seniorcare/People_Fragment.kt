package com.example.seniorcare

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
                when (i.id) {
                    R.id.person1 -> {
                        obj.newRegistrationDialogBox(requireContext())
                    }
                    R.id.person2 -> {
                        obj.newRegistrationDialogBox(requireContext())

                    }
                    R.id.person3 -> {
                        obj.newRegistrationDialogBox(requireContext())

                    }
                    R.id.person4 -> {
                        obj.newRegistrationDialogBox(requireContext())

                    }
                    R.id.person5 -> {
                        obj.newRegistrationDialogBox(requireContext())

                    }
                    R.id.person6 -> {
                        obj.newRegistrationDialogBox(requireContext())

                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}