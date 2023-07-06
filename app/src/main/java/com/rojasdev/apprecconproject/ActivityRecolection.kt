package com.rojasdev.apprecconproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.rojasdev.apprecconproject.alert.alertAddRecolector
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.databinding.ActivityRecolectionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityRecolection : AppCompatActivity() {
    private lateinit var binding: ActivityRecolectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRecolectionBinding.inflate(layoutInflater)
         super.onCreate(savedInstanceState)
          setContentView(binding.root)

    }

}