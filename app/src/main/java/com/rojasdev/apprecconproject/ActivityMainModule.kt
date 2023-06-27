package com.rojasdev.apprecconproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rojasdev.apprecconproject.alert.alertSettings
import com.rojasdev.apprecconproject.alert.alertWelcome
import com.rojasdev.apprecconproject.databinding.ActivityMainModuleBinding

class ActivityMainModule : AppCompatActivity() {
    lateinit var binding: ActivityMainModuleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainModuleBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        alertWelcome{
            alertSettings{

            }.show(supportFragmentManager, "dialog")
        }.show(supportFragmentManager,"dialog")
    }

}