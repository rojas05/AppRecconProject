package com.rojasdev.apprecconproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rojasdev.apprecconproject.databinding.ActivitySettingsBinding

class ActivitySettings : AppCompatActivity() {
    lateinit var binding : ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}