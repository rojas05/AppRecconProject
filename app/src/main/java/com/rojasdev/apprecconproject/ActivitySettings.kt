package com.rojasdev.apprecconproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rojasdev.apprecconproject.alert.alertSettingsUpdate
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.entities.SettingEntity
import com.rojasdev.apprecconproject.databinding.ActivitySettingsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivitySettings : AppCompatActivity() {
    lateinit var binding : ActivitySettingsBinding
    var idNoAliment : Int? = null
    var idYesAliment : Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = getString(R.string.settingsAliment )

        getNoAliment()
        getYesAliment()

        binding.btUpdateNoAliment.setOnClickListener {
            animatedAlert.animatedClick(binding.cvNoAliment)
            alertSettingsUpdate(getString(R.string.no_alimentacion),"no",idNoAliment!!){
                insertNewSetting(it){
                    getNoAliment()
                }
            }.show(supportFragmentManager,"dialog")
        }

        binding.btUpdateYesAliment.setOnClickListener {
            animatedAlert.animatedClick(binding.cvYesAliment)
            alertSettingsUpdate(getString(R.string.si_alimentacion),"yes",idYesAliment!!){
                insertNewSetting(it){
                    getYesAliment()
                }
            }.show(supportFragmentManager,"dialog")
        }
    }
    private fun getNoAliment(){
        animatedAlert.animatedCv(binding.cvNoAliment)
        CoroutineScope(Dispatchers.IO).launch{
            val query = AppDataBase.getInstance(this@ActivitySettings).SettingDao().getAliment("no")
            launch(Dispatchers.Main) {
                idNoAliment = query[0].Id
                price.priceSplit(query[0].cost){
                    binding.tvNoAliment.text = it
                }
            }
        }
    }

    private fun getYesAliment(){
        animatedAlert.animatedCv(binding.cvYesAliment)
        CoroutineScope(Dispatchers.IO).launch{
            val query = AppDataBase.getInstance(this@ActivitySettings).SettingDao().getAliment("yes")
            launch(Dispatchers.Main) {
                idYesAliment = query[0].Id
                price.priceSplit(query[0].cost){
                    binding.tvYesAliment.text = it
                }
            }
        }
    }

    private fun insertNewSetting(setting: SettingEntity,ready:()->Unit) {
        val newSetting = SettingEntity(
            null,
            setting.feeding,
            setting.cost,
            "active"
        )
        CoroutineScope(Dispatchers.IO).launch{
            AppDataBase.getInstance(this@ActivitySettings).SettingDao().Insertconfig(newSetting)
            AppDataBase.getInstance(this@ActivitySettings).SettingDao().UpdateConfig(setting.Id,"archived")
            launch(Dispatchers.Main) {
                ready()
            }
        }
    }
}