package com.example.seniorcare

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.seniorcare.databinding.ActivitySavedPersonInfoBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class savedPersonInfo : AppCompatActivity() {
    private lateinit var binding: ActivitySavedPersonInfoBinding

    private val GALLERY_REQUEST_CODE = 1000

    private val TAG = "Monotosh_People_Activity"

    private var personKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedPersonInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize personKey properly
        personKey = intent.getStringExtra("Person_unique_key")

        // Load the saved image URI from shared preferences
        val savedImageUri = getSavedImageUri()
        if (savedImageUri != null) {
            binding.photoSavedPersonInfo.setImageURI(savedImageUri)
        }

        binding.changePicBtn.setOnClickListener {
            val iGallery = Intent(Intent.ACTION_PICK)
            iGallery.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            startActivityForResult(iGallery, GALLERY_REQUEST_CODE)
        }

        // Retrieve and set the person information
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


        binding.removeSavedPersonInfo.setOnClickListener {
            personKey?.let { it1 -> obj().deleteSavePersonInfo(this, it1) }
            val sharedPreferences = this.getSharedPreferences("PeopleInfoPhoto", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.remove("${personKey}_Photo")
            editor.apply()
            Toast.makeText(this, "Information Deleted", Toast.LENGTH_SHORT).show()
            finish()
        }



    }






    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imgGallery = binding.photoSavedPersonInfo

        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            val imageUri = data?.data
            if (imageUri != null) {
                try {
                    // Save the image locally
                    val savedImageUri = saveImageToLocalDirectory(imageUri)
                    if (savedImageUri != null) {
                        // Update ImageView with the saved image URI
                        imgGallery.setImageURI(savedImageUri)
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

    fun getSavedImageUri(): Uri? {
        val sharedPreferences = this.getSharedPreferences("PeopleInfoPhoto", Context.MODE_PRIVATE)
        val savedImageUriString = sharedPreferences.getString("${personKey}_Photo", null)
        return if (savedImageUriString != null) Uri.parse(savedImageUriString) else null
    }
}
