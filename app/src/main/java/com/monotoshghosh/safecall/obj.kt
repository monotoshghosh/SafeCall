package com.monotoshghosh.safecall


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView

class obj {


    fun newRegistrationDialogBox (context:Context,personKey: String? = null, listener: OnProfileUpdatedListener? = null){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialogboxnewregis)
        dialog.window?.setBackgroundDrawableResource(R.drawable.rectshape)
        dialog.setCancelable(false)
        dialog.show()
        objSound.btnSoundDialogOpen(context as Activity)

        val exitBtn = dialog.findViewById<Button>(R.id.dialogExitBtn)
        exitBtn.setOnClickListener {
            if(!obj().isUserInfoSaved(context)){
                Toast.makeText(context,"Please fill the Details", Toast.LENGTH_SHORT).show()
                objVibration.vibrate(context)
                objSound.btnSoundError(context)
            }
            else{
                dialog.dismiss()
                objSound.btnSound(context as Activity)
            }

        }



        val nameInput = dialog.findViewById<EditText>(R.id.NewRegName)
        val ageInput = dialog.findViewById<EditText>(R.id.NewRegAge)
        val bloodGroupInput = dialog.findViewById<EditText>(R.id.NewRegBloogGroup)
        val locationInput = dialog.findViewById<EditText>(R.id.NewRegLoc)
        val phoneNoInput = dialog.findViewById<EditText>(R.id.NewRegPhNo)



        val submitBtn = dialog.findViewById<CardView>(R.id.dialogSubmitBtn)
        submitBtn.setOnClickListener {
            val name = nameInput.text.toString()
            val age = ageInput.text.toString()
            val bloodGroup = bloodGroupInput.text.toString()
            val location = locationInput.text.toString()
            val phoneNo = phoneNoInput.text.toString()

            if(name.isNotEmpty() && age.isNotEmpty() && bloodGroup.isNotEmpty() && location.isNotEmpty() && phoneNo.isNotEmpty()) {
                if (personKey == null) {
                    saveUserInfo(context, name, age, bloodGroup, location, phoneNo)
                } else {
                    savePersonInfo(context, personKey, name, age, bloodGroup, location, phoneNo)
                }


                dialog.dismiss()
                Toast.makeText(context, "Information Saved", Toast.LENGTH_SHORT).show()
                objSound.btnSound(context)
                listener?.onProfileUpdated() // Trigger the callback

            }
            else{
                Toast.makeText(context, "Please fill all the Details", Toast.LENGTH_SHORT).show()
                objVibration.vibrate(context)
                objSound.btnSoundError(context)
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
        editor.putString("Phone", phoneNo)
        editor.apply()

    }

    fun isUserInfoSaved(context: Context): Boolean {             // THIS IS CHECK IF DATA IS SAVED OR NOT
        val sharedPreferences = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        return sharedPreferences.contains("Name") &&
                sharedPreferences.contains("Age") &&
                sharedPreferences.contains("Blood Group") &&
                sharedPreferences.contains("Location") &&
                sharedPreferences.contains("Phone")
    }

    private fun savePersonInfo(context: Context, personKey: String, name: String, age: String, bloodGroup: String, location: String, phoneNo: String) {
        val sharedPreferences = context.getSharedPreferences("PeopleInfo", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("${personKey}_Name", name)
        editor.putString("${personKey}_Age", age)
        editor.putString("${personKey}_BloodGroup", bloodGroup)
        editor.putString("${personKey}_Location", location)
        editor.putString("${personKey}_Phone", phoneNo)
        editor.apply()
    }

    fun deleteSavePersonInfo(context: Context,personKey: String){
        val sharedPreferences = context.getSharedPreferences("PeopleInfo", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("${personKey}_Name")
        editor.remove("${personKey}_Age")
        editor.remove("${personKey}_BloodGroup")
        editor.remove("${personKey}_Location")
        editor.remove("${personKey}_Phone")
        editor.apply()
    }












}