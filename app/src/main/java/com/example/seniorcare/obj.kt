package com.example.seniorcare

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView

class obj {

    fun newRegistrationDialogBox (context:Context){
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

        val photoInput = dialog.findViewById<CardView>(R.id.NewRegProfilePic)
        val nameInput = dialog.findViewById<EditText>(R.id.NewRegName)
        val ageInput = dialog.findViewById<EditText>(R.id.NewRegAge)
        val bloodgGroupInput = dialog.findViewById<EditText>(R.id.NewRegBloogGroup)
        val locationInput = dialog.findViewById<EditText>(R.id.NewRegLoc)
        val phoneNoInput = dialog.findViewById<EditText>(R.id.NewRegPhNo)

        submitBtn.setOnClickListener {
//            val photo = photoInput.                       // THIS IS LEFT FOR IMAGE
            val name = nameInput.text.toString()
            val age = ageInput.text.toString()
            val bloodGroup = bloodgGroupInput.text.toString()
            val location = locationInput.text.toString()
            val phoneNo = phoneNoInput.text.toString()

            if(name.isNotEmpty() && age.isNotEmpty() && bloodGroup.isNotEmpty() && location.isNotEmpty() && phoneNo.isNotEmpty()) {
                saveUserInfo(context, name, age, bloodGroup, location, phoneNo)
                dialog.dismiss()
                Toast.makeText(context, "Information Saved", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context, "Please fill all the Details", Toast.LENGTH_SHORT).show()
            }
            
        }

    }
    private fun saveUserInfo(context: Context, name :String, age:String, bloodGroup:String, location :String, phoneNo:String){
        val sharedPreferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("Name", name)
        editor.putString("Age",age)
        editor.putString("Blood Group",bloodGroup)
        editor.putString("Location", location)
        editor.putString("Phone No", phoneNo)
        editor.apply()

    }




}