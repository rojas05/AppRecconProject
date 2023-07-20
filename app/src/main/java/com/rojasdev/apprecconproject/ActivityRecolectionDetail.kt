package com.rojasdev.apprecconproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.adapters.adpaterRvRecolection
import com.rojasdev.apprecconproject.alert.alertCollectionUpdate
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.dataModel.collectorCollection
import com.rojasdev.apprecconproject.data.entities.RecollectionEntity
import com.rojasdev.apprecconproject.databinding.ActivityRecolectionDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityRecolectionDetail : AppCompatActivity() {

    lateinit var binding: ActivityRecolectionDetailBinding
        private lateinit var adapter: adpaterRvRecolection
        private lateinit var collectionUpdate: List<collectorCollection>

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRecolectionDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val idCollector = intent.getIntExtra("userId", 0)
        val userName = intent.getStringExtra("userName")

        title = userName

        getRecollection(idCollector)

        binding.btnUpdate.setOnClickListener {
           alertCollectionUpdate(
               collectionUpdate[0].PK_ID_Recoleccion,
               collectionUpdate[0].PK_ID_Recolector,
               collectionUpdate[0].Alimentacion,
               collectionUpdate[0].Cantidad,
               userName!!
           ){
               updateRecoleccion(it,idCollector)
           }.show(supportFragmentManager,"dialog")
        }

    }

    private fun updateRecoleccion(it: RecollectionEntity, idCollector: Int){
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(this@ActivityRecolectionDetail).RecollectionDao().updateCollection(it.ID!!,it.collector,it.total,it.setting)
            launch(Dispatchers.Main){
                getRecollection(idCollector)
                Toast.makeText(this@ActivityRecolectionDetail, "Se actualizo la ultima Recoleccion", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getRecollection(idCollector: Int, userState: String = "active") {
        CoroutineScope(Dispatchers.IO).launch{
            val allRecolection = AppDataBase.getInstance(this@ActivityRecolectionDetail).RecolectoresDao().getCollectorAndCollection(userState, idCollector)
                launch(Dispatchers.Main) {
                    collectionUpdate = listOf(allRecolection[0])
                    adapter = adpaterRvRecolection(allRecolection) {
                        // Update Collection
                        alertUpdateRecollection(it, idCollector)
                    }
                        binding.tvKg.text = allRecolection[0].Cantidad.toString() + "Kg"
                        binding.rvRecolections.adapter = adapter
                        binding.rvRecolections.layoutManager = LinearLayoutManager(this@ActivityRecolectionDetail)

                    if (allRecolection[0].Alimentacion == "yes") {
                        binding.tvFeending.text = "Alimentación: Si"
                    } else {
                        binding.tvFeending.text = "Alimentación: No"
                    }

                    price.priceSplit(allRecolection[0].result.toInt()){
                        binding.tvTotal.text = "Total a Pagar: ${it}"
                    }

                    price.priceSplit(allRecolection[0].Precio.toInt()){
                        binding.tvPrice.text = "Presio: ${it}"
                    }
                }
        }
    }

    private fun alertUpdateRecollection(it:collectorCollection, idCollector: Int){
        alertCollectionUpdate(
            it.PK_ID_Recoleccion,
            idCollector,
            it.Alimentacion,
            it.Cantidad,
            it.name_recolector
        ){
            updateCollection(it, idCollector)
        }.show(supportFragmentManager,"dialog")
    }

    private fun updateCollection(it:RecollectionEntity, idCollector: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(this@ActivityRecolectionDetail).RecollectionDao().updateCollection(it.ID!!,it.collector,it.total,it.setting)
            launch(Dispatchers.Main){
                getRecollection(idCollector)
                Toast.makeText(this@ActivityRecolectionDetail, "Se actializo la Recoleccion", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
