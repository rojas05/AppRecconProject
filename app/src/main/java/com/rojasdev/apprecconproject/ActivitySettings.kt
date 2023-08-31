package com.rojasdev.apprecconproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.adapters.adapterRvSettings
import com.rojasdev.apprecconproject.alert.alertSettingsUpdate
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.customSnackbar
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
    var priceYesAliment : Int? = null
    var priceNoAliment : Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = getString(R.string.historySettings )

        getNoAliment()
        getYesAliment()

        binding.btUpdateNoAliment.setOnClickListener {
            animatedAlert.animatedClick(binding.cvNoAliment)
            alertSettingsUpdate(getString(R.string.no_alimentacion),"no",idNoAliment!!,priceNoAliment!!){
                insertNewSetting(it){
                    getNoAliment()
                    setupRecyclerView()
                }
            }.show(supportFragmentManager,"dialog")
        }

        binding.btUpdateYesAliment.setOnClickListener {
            animatedAlert.animatedClick(binding.cvYesAliment)
            alertSettingsUpdate(getString(R.string.si_alimentacion),"yes",idYesAliment!!,priceYesAliment!!){
                insertNewSetting(it){
                    getYesAliment()
                    setupRecyclerView()
                }
            }.show(supportFragmentManager,"dialog")
        }

        binding.btViewAlimentArchived.setOnClickListener {
            binding.clPreciosVigentes.visibility = View.GONE
            setupRecyclerViewArchived()
        }

        binding.btExit.setOnClickListener {
            setupRecyclerView()
            binding.clPreciosVigentes.visibility = View.VISIBLE
        }

        setupRecyclerView()
    }
    private fun getNoAliment(){
        animatedAlert.animatedCv(binding.cvNoAliment)
        CoroutineScope(Dispatchers.IO).launch{
            val query = AppDataBase.getInstance(this@ActivitySettings).SettingDao().getAliment("no")
            launch(Dispatchers.Main) {
                idNoAliment = query[0].Id
                price.priceSplit(query[0].cost){
                    priceNoAliment = query[0].cost
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
                    priceYesAliment = query[0].cost
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

    private fun setupRecyclerView() {
        binding.rvSetTingHistory.setPadding(0,0,0,0)
        CoroutineScope(Dispatchers.IO).launch{
            val query = AppDataBase.getInstance(this@ActivitySettings).SettingDao().getAlimentArchived()
            launch(Dispatchers.Main) {
                binding.rvSetTingHistory.apply {
                if(query.isEmpty()){
                    noHystory()
                }else{
                    visibilityButon(query.size)
                        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        adapter = adapterRvSettings(query) {}
                    }
                }
            }
        }
    }

    private fun noHystory() {
        title = "configuracion de precios"
        binding.btViewAlimentArchived.visibility = View.GONE
        binding.btExit.visibility = View.GONE
    }

    private fun visibilityButon(size: Int) {
        title = "precios anteriores"
        binding.btExit.visibility = View.GONE
        if(size > 4)
            binding.btViewAlimentArchived.visibility = View.VISIBLE
        else
            binding.btViewAlimentArchived.visibility = View.GONE
    }

    private fun setupRecyclerViewArchived() {
        val heihgt = getResources().getDisplayMetrics().widthPixels
        val setPadding = heihgt.div(3.9)
        binding.rvSetTingHistory.setPadding(0,0,0,setPadding.toInt())
        CoroutineScope(Dispatchers.IO).launch{
            val query = AppDataBase.getInstance(this@ActivitySettings).SettingDao().getAlimentArchived()
            launch(Dispatchers.Main) {
                binding.rvSetTingHistory.apply {
                    title = "todos los precios anteriores"
                    binding.btViewAlimentArchived.visibility = View.GONE
                    binding.btExit.visibility = View.VISIBLE
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adapter = adapterRvSettings(query) {
                        mensagge(it)
                    }
                }
            }
        }
    }

    private fun mensagge(it: SettingEntity) {
        val mensagge: String
        if(it.feeding == "yes")
            mensagge = getString(R.string.si_alimentacion)
        else
            mensagge = getString(R.string.no_alimentacion)
        customSnackbar.showCustomSnackbar(binding.rvSetTingHistory,"$mensagge\n${it.cost}" )
    }


}