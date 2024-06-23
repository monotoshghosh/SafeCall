package com.example.seniorcare

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.seniorcare.databinding.ActivitySavedPersonInfoBinding

class savedPersonInfo : AppCompatActivity() {
    private lateinit var binding: ActivitySavedPersonInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivitySavedPersonInfoBinding.inflate(layoutInflater)

//        enableEdgeToEdge()
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        var personKey = intent.getStringExtra("Person_unique_key")

        val sharedPreferences = this.getSharedPreferences("PeopleInfo", Context.MODE_PRIVATE)

        val name = sharedPreferences.getString("${personKey}_Name","NULL")
        val age = sharedPreferences.getString("${personKey}_Age","NULL")
        val bloodGroup = sharedPreferences.getString("${personKey}_BloodGroup","NULL")
        val location = sharedPreferences.getString("${personKey}_Location","NULL")
        val phone = sharedPreferences.getString("${personKey}_Phone","NULL")

        binding.nameSavedPersonInfo.text = name

        binding.ageSavedPersonInfo.text = age

        binding.bloodgroupSavedPersonInfo.text = bloodGroup

        binding.locationSavedPersonInfo.text = location

        binding.phoneNoSavedPersonInfo.text = phone



    }
}