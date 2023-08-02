package com.rojasdev.apprecconproject

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.adapters.adpaterRvRecolection
import com.rojasdev.apprecconproject.alert.alertCollectionUpdate
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.dataModel.collecionTotalCollector
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
        private lateinit var collectionTotal: List<collecionTotalCollector>

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRecolectionDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val idCollector = intent.getIntExtra("userId", 0)
        val userName = intent.getStringExtra("userName")

        title = userName

        getRecollection(idCollector)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getRecollection(idCollector: Int, userState: String = "active") {
        CoroutineScope(Dispatchers.IO).launch{
            val collection= AppDataBase.getInstance(this@ActivityRecolectionDetail).RecolectoresDao().getCollectorAndCollection(userState,idCollector)
            val totalRecolection = AppDataBase.getInstance(this@ActivityRecolectionDetail).RecolectoresDao().getCollectorAndCollectionTotal(idCollector)
                launch(Dispatchers.Main) {
                    collectionUpdate = listOf(collection[0])
                    collectionTotal = listOf(totalRecolection[0])
                    adapter = adpaterRvRecolection(collection) {
                        // Update Collection
                        alertUpdateRecollection(it, idCollector)
                    }
                        price.priceSplit(collection[0].result.toInt()){
                            binding.tvTotal.text = "Total: ${it}"
                        }

                        binding.tvTitle.text = "Total Recolectado: ${totalRecolection[0].kg_collection}"
                        binding.rvRecolections.adapter = adapter
                        binding.rvRecolections.layoutManager = LinearLayoutManager(this@ActivityRecolectionDetail)

                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateCollection(it:RecollectionEntity, idCollector: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(this@ActivityRecolectionDetail).RecollectionDao().updateCollection(it.ID!!,it.date,it.time,it.collector,it.total,it.setting)
            launch(Dispatchers.Main){
                getRecollection(idCollector)
                Toast.makeText(this@ActivityRecolectionDetail, "Se actializo la Recoleccion", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
