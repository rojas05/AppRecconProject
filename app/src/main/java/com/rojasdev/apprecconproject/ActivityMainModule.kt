package com.rojasdev.apprecconproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rojasdev.apprecconproject.alert.alertSettings
import com.rojasdev.apprecconproject.alert.alertWelcome
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.entities.SettingEntity
import com.rojasdev.apprecconproject.databinding.ActivityMainModuleBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityMainModule : AppCompatActivity() {
    lateinit var binding: ActivityMainModuleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainModuleBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        checkRegister()

        binding.cvInformes.setOnClickListener {
            animatedAlert.animatedClick(binding.cvInformes)
        }

        binding.cvCollection.setOnClickListener {
            animatedAlert.animatedClick(binding.cvCollection)
        }
    }

    private fun alerts(){
        alertWelcome{
            alertSettings{
                insertSettings(it)
            }.show(supportFragmentManager, "dialog")
        }.show(supportFragmentManager,"dialog")
    }

    private fun insertSettings(settings: SettingEntity){
        preferences()
        CoroutineScope(Dispatchers.IO).launch{
            AppDataBase.getInstance(this@ActivityMainModule).SettingDao().Insertconfig(settings)
        }
    }

    private fun preferences (){
        val preferences = getSharedPreferences( "register", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("register","true")
        editor.apply()
    }

    private fun checkRegister(){
        val preferences = getSharedPreferences( "register", Context.MODE_PRIVATE)
        val register = preferences.getString("register","")
        if(register != "true"){
            alerts()
        }
    }

}