package com.example.seniorcare

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.Toast

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




    }
}