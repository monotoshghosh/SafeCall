package com.example.seniorcare

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.seniorcare.databinding.ActivityMainScreenBinding

class MainScreen : AppCompatActivity() {
    private lateinit var binding: ActivityMainScreenBinding

    private var isAppOpenedFirstTime = true // THIS IS FOR THE SOUND TO CHECK IF THE APP OPEN FIRST TIME


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
//        enableEdgeToEdge()
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        window.apply {
            statusBarColor =ContextCompat.getColor(this@MainScreen,android.R.color.white)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        if(!obj().isUserInfoSaved(this)){
            obj().newRegistrationDialogBox(this)
        }

//        replaceWithFragment(Home_Fragment())

        binding.bnView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.itemPeopleId -> replaceWithFragment(People_Fragment())
                R.id.itemHomeId -> replaceWithFragment(Home_Fragment())
                R.id.itemProfileId -> replaceWithFragment(Profile_Fragment())

            else ->{}
            }
            true
        }

        if(savedInstanceState == null){
            binding.bnView.selectedItemId =R.id.itemHomeId
        }



    }
    private fun replaceWithFragment(fragment : Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()

        if (isAppOpenedFirstTime){
            isAppOpenedFirstTime = false
        }
        else{
            objSound.btnSound(this)
        }
    }




}


// People Fragment:
//=================
// 1.  Layout Change
// 2.  Remove Sound
// 3. Info save Dialog --> empty box deny submit


// Home Fragment:
//===============
// 1. When start Pressed Check if all permissions are enabled
// 2. Siren Sound


// Profile Fragment:
//=================

