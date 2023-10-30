package com.rojasdev.apprecconproject

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.rojasdev.apprecconproject.controller.checkAssistant
import com.rojasdev.apprecconproject.controller.sifrado
import com.rojasdev.apprecconproject.controller.textToSpeech
import kotlinx.coroutines.*
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      checkAssistant.start(this,
          {
              CoroutineScope(Dispatchers.IO).launch {
                  textToSpeech().start(this@MainActivity,greet()){
                      startActivity(Intent(this@MainActivity,ActivityMainModule::class.java))
                  }
              }
          }, {
              startActivity(Intent(this@MainActivity,ActivityMainModule::class.java))
          })

        val key = sifrado().generateKey()

        sifrado().encryptResources(key,"AppRecconProject\\app\\src\\main")
    }
    private fun greet():String{
        val hour = Calendar.getInstance()
        val hourCycle = hour.get(Calendar.HOUR_OF_DAY)
        return when {
            hourCycle < 12 -> "Buenos días"
            hourCycle in 12..17 -> "Buenas tardes"
            else -> "Buenas noches"
        }
    }

}
