package com.monotoshghosh.safecall

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.monotoshghosh.safecall.databinding.PeopleFragmentBinding

class People_Fragment : Fragment(R.layout.people_fragment) {

    private var _binding: PeopleFragmentBinding? = null
    private val binding get() = _binding!!

    private var mInterstitialAd: InterstitialAd? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PeopleFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {       //    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onResume()            //         super.onViewCreated(view, savedInstanceState)

        MobileAds.initialize(requireContext()) {}
        loadinterstitialAd()

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

                fun ifCardPersonSaved(){
                    Toast.makeText(requireContext(), "Person Already Saved !!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(), savedPersonInfo::class.java)
                    intent.putExtra("Person_unique_key", personKey)
                    startActivity(intent)
                }

                card.setOnClickListener {
                    if (!isPersonInfoSaved(personKey, it.context)) {
                        obj().newRegistrationDialogBox(it.context, personKey)
                    } else {
                        if (mInterstitialAd != null) {
                            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    // Ad was dismissed, launch the image picker
                                    ifCardPersonSaved()
                                    loadinterstitialAd() // Load a new ad after showing the current one
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                    // If the ad fails to show, also launch the image picker
                                    ifCardPersonSaved()
                                    loadinterstitialAd() // Load a new ad after the failure
                                }

                                override fun onAdShowedFullScreenContent() {
                                    // Called when ad is shown
                                    mInterstitialAd = null // Set the ad reference to null to load a new ad
                                }
                            }
                            mInterstitialAd?.show(requireActivity())
                        } else {
                            ifCardPersonSaved()
                            loadinterstitialAd() // Load a new ad in case there is no ad loaded
                        }


//                        Toast.makeText(requireContext(), "Person Already Saved !!", Toast.LENGTH_SHORT).show()
//                        val intent = Intent(requireContext(), savedPersonInfo::class.java)
//                        intent.putExtra("Person_unique_key", personKey)
//                        startActivity(intent)
                    }
                }
            }



        }
    }


    fun loadinterstitialAd(){
        var adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireContext(),"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })
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
