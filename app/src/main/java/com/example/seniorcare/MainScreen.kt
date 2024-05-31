package com.example.seniorcare

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.seniorcare.databinding.ActivityMainScreenBinding

class MainScreen : AppCompatActivity() {
    private lateinit var binding: ActivityMainScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        replaceWithFragment(Home_Fragment())

        binding.bnView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.itemPeopleId -> replaceWithFragment(People_Fragment())
                R.id.itemHomeId -> replaceWithFragment(Home_Fragment())
                R.id.itemProfileId -> replaceWithFragment(Profile_Fragment())

            else ->{}
            }
            true
        }

        binding.button.setOnClickListener {
            if(binding.button.text.toString()=="START"){
                binding.button.text= "STOP"
                binding.button.setBackgroundColor(resources.getColor(R.color.red))
            }
            else{
                binding.button.text= "START"
                binding.button.setBackgroundColor(resources.getColor(R.color.green))
            }
        }

    }
    private fun replaceWithFragment(fragment : Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
    }


}