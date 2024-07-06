package com.monotoshghosh.safecall

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.monotoshghosh.safecall.databinding.ActivitySavedPersonInfoBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class savedPersonInfo : AppCompatActivity(), OnProfileUpdatedListener {
    private lateinit var binding: ActivitySavedPersonInfoBinding

    private val GALLERY_REQUEST_CODE = 1000

    private val TAG = "Monotosh_People_Activity"

    private var personKey: String? = null

    private var mInterstitialAd: InterstitialAd? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedPersonInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.apply {
            statusBarColor = ContextCompat.getColor(this@savedPersonInfo,android.R.color.white)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        personKey = intent.getStringExtra("Person_unique_key")

        loadPersonInfo()

        binding.exitBtnSavedPersonInfo.setOnClickListener {
            finish()
        }

        MobileAds.initialize(this) {}
        loadinterstitialAd()

        binding.adminPhotoChangeBtn.setOnClickListener {

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
                mInterstitialAd?.show(this)
            } else {
                launchImagePicker()
                loadinterstitialAd() // Load a new ad in case there is no ad loaded
            }

        }

        binding.removeSavedPersonInfo.setOnClickListener {
            personKey?.let { it1 -> obj().deleteSavePersonInfo(this, it1) }
            val sharedPreferences = this.getSharedPreferences("PeopleInfoPhoto", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.remove("${personKey}_Photo")
            editor.apply()
            Toast.makeText(this, "Information Deleted", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.editSavedPersonInfo.setOnClickListener {
            obj().newRegistrationDialogBox(this, personKey, this)
        }
    }


    private fun launchImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    fun loadinterstitialAd(){
        var adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this,"ca-app-pub-8334546624219108/8014477264", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })
    }

    override fun onProfileUpdated() {
        loadPersonInfo()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            val imageUri = data?.data
            if (imageUri != null) {
                try {
                    val savedImageUri = saveImageToLocalDirectory(imageUri)
                    if (savedImageUri != null) {
                        binding.photoSavedPersonInfo.setImageURI(savedImageUri)
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

    private fun loadPersonInfo() {
        val savedImageUri = getSavedImageUri()
        if (savedImageUri != null) {
            binding.photoSavedPersonInfo.setImageURI(savedImageUri)
        } else {
            binding.photoSavedPersonInfo.setImageResource(R.drawable.profilepic1)
        }

        val sharedPreferences = this.getSharedPreferences("PeopleInfo", Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("${personKey}_Name", "NULL")
        val age = sharedPreferences.getString("${personKey}_Age", "NULL")
        val bloodGroup = sharedPreferences.getString("${personKey}_BloodGroup", "NULL")
        val location = sharedPreferences.getString("${personKey}_Location", "NULL")
        val phone = sharedPreferences.getString("${personKey}_Phone", "NULL")

        binding.nameSavedPersonInfo.text = name
        binding.ageSavedPersonInfo.text = age
        binding.bloodgroupSavedPersonInfo.text = bloodGroup
        binding.locationSavedPersonInfo.text = location
        binding.phoneNoSavedPersonInfo.text = phone
    }

    private fun saveImageToLocalDirectory(imageUri: Uri): Uri? {
        return try {
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val imageFile = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
            val outputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.close()
            FileProvider.getUriForFile(this, "com.example.seniorcare.fileprovider", imageFile)
        } catch (e: IOException) {
            Log.e(TAG, "Failed to save image: ${e.message}")
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show()
            null
        }
    }

    private fun saveImageUriToPreferences(imageUri: Uri) {
        val sharedPreferences = this.getSharedPreferences("PeopleInfoPhoto", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("${personKey}_Photo", imageUri.toString())
        editor.apply()
    }

    private fun getSavedImageUri(): Uri? {
        val sharedPreferences = this.getSharedPreferences("PeopleInfoPhoto", Context.MODE_PRIVATE)
        val savedImageUriString = sharedPreferences.getString("${personKey}_Photo", null)
        return if (savedImageUriString != null) Uri.parse(savedImageUriString) else null
    }
}
