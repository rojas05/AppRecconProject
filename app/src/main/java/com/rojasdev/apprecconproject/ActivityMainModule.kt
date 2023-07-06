package com.rojasdev.apprecconproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.rojasdev.apprecconproject.alert.alertAddRecolector
import com.rojasdev.apprecconproject.alert.alertSettings
import com.rojasdev.apprecconproject.alert.alertWelcome
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.data.entities.SettingEntity
import com.rojasdev.apprecconproject.databinding.ActivityMainModuleBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityMainModule : AppCompatActivity() {

    lateinit var binding: ActivityMainModuleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainModuleBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        checkRegister()

        binding.cvInformes.setOnClickListener {
            animatedAlert.animatedClick(binding.cvInformes)
            startActivity(Intent(this,ActivityInformes::class.java))
        }

        binding.cvCollection.setOnClickListener {
            animatedAlert.animatedClick(binding.cvCollection)
                alertAddRecolcetor()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings -> startActivity(Intent(this,ActivitySettings::class.java))
            R.id.support -> Toast.makeText(this, "trabajando...", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
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

    private fun alertAddRecolcetor() {
        alertAddRecolector{
            insertRecolector(it)
            allUser()
        }.show(supportFragmentManager, "dialog")
    }

    private fun insertRecolector(recolector: RecolectoresEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(this@ActivityMainModule).RecolectoresDao().add(recolector)
        }
        Toast.makeText(this@ActivityMainModule, "Se agrego un nuevo miembro ${recolector.name}", Toast.LENGTH_SHORT).show()
    }

    private fun allUser() {
        CoroutineScope(Dispatchers.IO).launch {
            val allRecolector = AppDataBase.getInstance(this@ActivityMainModule).RecolectoresDao()
            val user = withContext(Dispatchers.IO){
                allRecolector.getAllRecolector()
            }
                if(user != null) {
                    startActivity(Intent(this@ActivityMainModule, ActivityRecolection::class.java ))
                } else {
                    finish()
                  }
        }
    }

}