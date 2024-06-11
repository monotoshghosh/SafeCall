package com.example.seniorcare

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView

class obj {

    fun newRegistrationDialogBox (context:Context,personKey: String? = null){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialogboxnewregis)
        dialog.window?.setBackgroundDrawableResource(R.drawable.rectshape)
        dialog.setCancelable(false)
        dialog.show()

        val exitBtn = dialog.findViewById<Button>(R.id.dialogExitBtn)
        exitBtn.setOnClickListener {
            dialog.dismiss()
        }
        
        val submitBtn = dialog.findViewById<Button>(R.id.dialogSubmitBtn)
        submitBtn.setOnClickListener {
            Toast.makeText(context, "Submit Button Pressed", Toast.LENGTH_SHORT).show()
        }

        val imageProfile = dialog.findViewById<ImageView>(R.id.NewRegProfilePicImgView)
        val nameInput = dialog.findViewById<EditText>(R.id.NewRegName)
        val ageInput = dialog.findViewById<EditText>(R.id.NewRegAge)
        val bloodgGroupInput = dialog.findViewById<EditText>(R.id.NewRegBloogGroup)
        val locationInput = dialog.findViewById<EditText>(R.id.NewRegLoc)
        val phoneNoInput = dialog.findViewById<EditText>(R.id.NewRegPhNo)


        var selectedImageUri: Uri? = null

        imageProfile.setOnClickListener {
            // Code to open image picker and get image URI
            openImagePicker(context) { uri ->
                selectedImageUri = uri
                imageProfile.setImageURI(uri)
            }
        }

        submitBtn.setOnClickListener {
//            val photo = photoInput.                       // THIS IS LEFT FOR IMAGE
            val name = nameInput.text.toString()
            val age = ageInput.text.toString()
            val bloodGroup = bloodgGroupInput.text.toString()
            val location = locationInput.text.toString()
            val phoneNo = phoneNoInput.text.toString()

            if(name.isNotEmpty() && age.isNotEmpty() && bloodGroup.isNotEmpty() && location.isNotEmpty() && phoneNo.isNotEmpty()) {
                if (personKey == null) {
                    saveUserInfo(context, name, age, bloodGroup, location, phoneNo,selectedImageUri)
                } else {
                    savePersonInfo(context, personKey, name, age, bloodGroup, location, phoneNo,selectedImageUri)
                }
                dialog.dismiss()
                Toast.makeText(context, "Information Saved", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context, "Please fill all the Details", Toast.LENGTH_SHORT).show()
            }
            
        }

    }
    private fun saveUserInfo(context: Context, name :String, age:String, bloodGroup:String, location :String, phoneNo:String,imageUri: Uri?){
        val sharedPreferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("Name", name)
        editor.putString("Age",age)
        editor.putString("Blood Group",bloodGroup)
        editor.putString("Location", location)
        editor.putString("Phone", phoneNo)
        if (imageUri != null) {
            editor.putString("ImageUri", imageUri.toString())
        }
        editor.apply()

    }

    private fun savePersonInfo(context: Context, personKey: String, name: String, age: String, bloodGroup: String, location: String, phoneNo: String, imageUri: Uri?) {
        val sharedPreferences = context.getSharedPreferences("PeopleInfo", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("${personKey}_Name", name)
        editor.putString("${personKey}_Age", age)
        editor.putString("${personKey}_BloodGroup", bloodGroup)
        editor.putString("${personKey}_Location", location)
        editor.putString("${personKey}_Phone", phoneNo)
        if (imageUri != null) {
            editor.putString("${personKey}_ImageUri", imageUri.toString())
        }
        editor.apply()
    }

    private fun openImagePicker(context: Context, callback: (Uri) -> Unit) {
        // Implement the logic to open the image picker and return the selected image URI
    }




}