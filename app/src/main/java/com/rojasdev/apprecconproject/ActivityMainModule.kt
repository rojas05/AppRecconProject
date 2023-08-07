package com.rojasdev.apprecconproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.rojasdev.apprecconproject.alert.alertAddRecolector
import com.rojasdev.apprecconproject.alert.alertSettings
import com.rojasdev.apprecconproject.alert.alertWelcome
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
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

        title = "Precios "

        this.onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })

        checkRegister()

        binding.cvInformes.setOnClickListener {
            checkRegister()
            animatedAlert.animatedClick(binding.cvInformes)
            startActivity(Intent(this,ActivityInformes::class.java))
        }

        binding.cvCollection.setOnClickListener {
            checkRegister()
            animatedAlert.animatedClick(binding.cvCollection)
                checkCollection()
        }

        binding.btSettings.setOnClickListener {
            checkRegister()
            startActivity(Intent(this,ActivitySettings::class.java))
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.support -> Toast.makeText(this, "Trabajando...", Toast.LENGTH_SHORT).show()
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
            launch {
                checkRegister()
            }
        }
    }

    private fun preferences (){
        val preferences = getSharedPreferences( "register", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("register","true")
        editor.putString("collection","false")
        editor.apply()
    }

    private fun checkRegister(){
        val preferences = getSharedPreferences( "register", Context.MODE_PRIVATE)
        val register = preferences.getString("register","")
        if(register != "true"){
            alerts()
        }else{
            getNoAliment()
            getYesAliment()
        }
    }
    private fun getNoAliment(){
        CoroutineScope(Dispatchers.IO).launch{
            val query = AppDataBase.getInstance(this@ActivityMainModule).SettingDao().getAliment("no")
            launch(Dispatchers.Main) {
                price.priceSplit(query[0].cost){
                    binding.tvNoAliment.text = it
                }
            }
        }
    }

    private fun getYesAliment(){
        CoroutineScope(Dispatchers.IO).launch{
            val query = AppDataBase.getInstance(this@ActivityMainModule).SettingDao().getAliment("yes")
            launch(Dispatchers.Main) {
                price.priceSplit(query[0].cost){
                    binding.tvYesAliment.text = it
                }
            }
        }
    }

    private fun checkCollection(){
        val preferences = getSharedPreferences( "register", Context.MODE_PRIVATE)
        val collection = preferences.getString("collection","")
        if(collection != "true"){
            alertAddRecolcetor()
        }else{
            startActivity(Intent(this,ActivityRecolection::class.java))
        }
    }

    private fun alertAddRecolcetor() {
        alertAddRecolector(
            {
                insertRecolector(it)
            },
            {
                startActivity(Intent(this,ActivityRecolection::class.java))
            }
        ).show(supportFragmentManager, "dialog")
    }

    private fun insertRecolector(recolector: RecolectoresEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(this@ActivityMainModule).RecolectoresDao().add(recolector)
        }
        preferencesCollecion()
    }

    private fun preferencesCollecion() {
        val preferences = getSharedPreferences( "register", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("collection","true")
        editor.apply()
    }

}