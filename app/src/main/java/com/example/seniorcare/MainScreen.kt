package com.example.seniorcare

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
//            permissionAsk.checkAndRequestLocationPermission(this)
        }

        // Request both SMS and Location permissions
        permissionAsk.checkAndRequestPermissions(this)
//        permissionAsk.checkAndRequestSmsPermission(this)


        // Check if location is enabled
        if (!locationCheck.isLocationEnabled(this)) {
            locationCheck.promptEnableLocation(this)
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionAsk.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }




}

// App Name Change -- packages / all

// People Fragment:
//=================
// 1. Photo


// Home Fragment:
//===============


// Profile Fragment:
//=================
// 1. Photo

