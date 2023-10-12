package com.rojasdev.apprecconproject

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.rojasdev.apprecconproject.controller.checkAssistant
import com.rojasdev.apprecconproject.controller.textToSpeech
import com.rojasdev.apprecconproject.controller.timer
import kotlinx.coroutines.*
import java.time.LocalTime
import java.util.Calendar
import java.util.Timer
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      checkAssistant.start(
          this,
          {
              CoroutineScope(Dispatchers.IO).launch {
                  textToSpeech().start(this@MainActivity,greet()){
                      startActivity(Intent(this@MainActivity,ActivityMainModule::class.java))
                  }
              }
          },
          {
              startActivity(Intent(this@MainActivity,ActivityMainModule::class.java))
          })


    }
    fun greet():String{
        val hour = Calendar.getInstance()
        val hourCycle = hour.get(Calendar.HOUR_OF_DAY)
        return when {
            hourCycle < 12 -> "Buenos días"
            hourCycle in 12..17 -> "Buenas tardes"
            else -> "Buenas noches"
        }
    }
}
