package com.example.seniorcare

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.seniorcare.databinding.ProfileFragmentBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Profile_Fragment: Fragment(R.layout.profile_fragment), OnProfileUpdatedListener {

    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!


    private val TAG = "Monotosh_Profile"
    private val GALLERY_REQUEST_CODE = 1000

    private var mInterstitialAd: InterstitialAd? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MobileAds.initialize(requireContext()) {}
        loadinterstitialAd()



        val adminPhoto = binding.adminProfilePic
        val btnPhotoChange = binding.adminPhotoChangeBtn

        // Load the saved image URI from shared preferences
        val savedImageUri = getSavedImageUri()
        if (savedImageUri != null) {
            adminPhoto.setImageURI(savedImageUri)
        }

        btnPhotoChange.setOnClickListener {
            if (mInterstitialAd != null) {
                mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        // Ad was dismissed, launch the image picker
                        launchImagePicker()
                        loadinterstitialAd() // Load a new ad after showing the current one
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        // If the ad fails to show, also launch the image picker
                        launchImagePicker()
                        loadinterstitialAd() // Load a new ad after the failure
                    }

                    override fun onAdShowedFullScreenContent() {
                        // Called when ad is shown
                        mInterstitialAd = null // Set the ad reference to null to load a new ad
                    }
                }
                mInterstitialAd?.show(requireActivity())
            } else {
                launchImagePicker()
                loadinterstitialAd() // Load a new ad in case there is no ad loaded
            }
        }

        loadProfileData()

        binding.editBtnprofileFragment.setOnClickListener {
//            Toast.makeText(requireContext(), "Edit Box Opening", Toast.LENGTH_SHORT).show()
            obj().newRegistrationDialogBox(requireContext(),null,this) // here i am updating the details but not immdediately it is reflected when app reopen or fragmnt switch
//            objSound.btnSound(requireActivity())

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

    private fun launchImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val adminPhoto = binding.adminProfilePic

        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            val imageUri = data?.data
            if (imageUri != null) {
                try {
                    // Save the image locally
                    val savedImageUri = saveImageToLocalDirectory(imageUri)
                    if (savedImageUri != null) {
                        // Update ImageView with the saved image URI
                        adminPhoto.setImageURI(savedImageUri)
                        // Save the image URI to shared preferences
                        saveImageUriToPreferences(savedImageUri)
                    } else {
                        Log.e(TAG, "Failed to save image locally.")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error processing selected image: ${e.message}")
                }
            } else {
                Log.e(TAG, "Image URI is null.")
            }
        } else {
            Log.e(TAG, "Result code not OK or request code mismatch.")
        }
    }

    private fun saveImageToLocalDirectory(imageUri: Uri): Uri? {
        return try {
            val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val imageFile = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
            val outputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.close()
            FileProvider.getUriForFile(requireContext(), "com.example.seniorcare.fileprovider", imageFile)
        } catch (e: IOException) {
            Log.e(TAG, "Failed to save image: ${e.message}")
            Toast.makeText(requireContext(), "Failed to save image", Toast.LENGTH_SHORT).show()
            null
        }
    }

    private fun saveImageUriToPreferences(imageUri: Uri) {
        val sharedPreferences = requireContext().getSharedPreferences("UserInfoPhoto", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("Admin_Photo", imageUri.toString())
        editor.apply()
    }

    private fun getSavedImageUri(): Uri? {
        val sharedPreferences = requireContext().getSharedPreferences("UserInfoPhoto", Context.MODE_PRIVATE)
        val savedImageUriString = sharedPreferences.getString("Admin_Photo", null)
        return if (savedImageUriString != null) Uri.parse(savedImageUriString) else null
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}