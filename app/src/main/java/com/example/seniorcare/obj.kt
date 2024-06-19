package com.example.seniorcare


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext

class obj {

    fun testDialogBox (context:Context) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.test)
        dialog.window?.setBackgroundDrawableResource(R.drawable.rectshape)
        dialog.setCancelable(false)
        dialog.show()
        objSound.btnSoundDialogOpen(context as Activity)

    }


    fun savedPersonInfoDialogBox (context:Context,personKey: String) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.savedpersoninfodialogbox)
        dialog.window?.setBackgroundDrawableResource(R.drawable.rectshape)
        dialog.setCancelable(false)
        dialog.show()
        objSound.btnSoundDialogOpen(context as Activity)

        val exitBtn =dialog.findViewById<Button>(R.id.exitBtnSavedPersonInfoDilogBox)
        exitBtn.setOnClickListener {
            dialog.dismiss()
            objSound.btnSound(context)

        }



        val sharedPreferences = context.getSharedPreferences("PeopleInfo", Context.MODE_PRIVATE)

        val name = sharedPreferences.getString("${personKey}_Name","NULL")
        val age = sharedPreferences.getString("${personKey}_Age","NULL")
        val bloodGroup = sharedPreferences.getString("${personKey}_BloodGroup","NULL")
        val location = sharedPreferences.getString("${personKey}_Location","NULL")
        val phone = sharedPreferences.getString("${personKey}_Phone","NULL")

        val nameTextView = dialog.findViewById<TextView>(R.id.nameSavedPersonInfoDilogBox)
        nameTextView.text = name

        val ageTextView = dialog.findViewById<TextView>(R.id.ageSavedPersonInfoDilogBox)
        ageTextView.text = age

        val bloodGroupTextView = dialog.findViewById<TextView>(R.id.bloodgroupSavedPersonInfoDilogBox)
        bloodGroupTextView.text = bloodGroup

        val locationTextView = dialog.findViewById<TextView>(R.id.locationSavedPersonInfoDilogBox)
        locationTextView.text = location

        val phoneTextView = dialog.findViewById<TextView>(R.id.phoneNoSavedPersonInfoDilogBox)
        phoneTextView.text = phone




        val removeBtn = dialog.findViewById<CardView>(R.id.removeSavedPersonInfoDilogBox)
        removeBtn.setOnClickListener {
            obj().deleteSavePersonInfo(context,personKey)
            dialog.dismiss()
            Toast.makeText(context, "Person Removed", Toast.LENGTH_SHORT).show()
            objSound.btnSoundRemove(context)
        }


        val editBtn = dialog.findViewById<CardView>(R.id.editSavedPersonInfoDilogBox)
        editBtn.setOnClickListener {
            dialog.dismiss()
            obj().newRegistrationDialogBox(context,personKey)
//            objSound.btnSound(context)
        }


    }

    fun newRegistrationDialogBox (context:Context,personKey: String? = null, listener: OnProfileUpdatedListener? = null){
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialogboxnewregis)
        dialog.window?.setBackgroundDrawableResource(R.drawable.rectshape)
        dialog.setCancelable(false)
        dialog.show()
        objSound.btnSoundDialogOpen(context as Activity)

        val exitBtn = dialog.findViewById<Button>(R.id.dialogExitBtn)
        exitBtn.setOnClickListener {
            dialog.dismiss()
            objSound.btnSound(context as Activity)
        }
        
        val submitBtn = dialog.findViewById<Button>(R.id.dialogSubmitBtn)


        val nameInput = dialog.findViewById<EditText>(R.id.NewRegName)
        val ageInput = dialog.findViewById<EditText>(R.id.NewRegAge)
        val bloodGroupInput = dialog.findViewById<EditText>(R.id.NewRegBloogGroup)
        val locationInput = dialog.findViewById<EditText>(R.id.NewRegLoc)
        val phoneNoInput = dialog.findViewById<EditText>(R.id.NewRegPhNo)



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

    private fun deleteSavePersonInfo(context: Context,personKey: String){
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