package com.rojasdev.apprecconproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.adapters.adpaterRvRecolection
import com.rojasdev.apprecconproject.alert.alertCollectionUpdate
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
               collectionUpdate,
               userName!!
           ){
               updateRecoleccion(it)
           }.show(supportFragmentManager,"dialog")
        }

    }

    private fun updateRecoleccion(it: RecollectionEntity){
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(this@ActivityRecolectionDetail).RecollectionDao().updateCollection(it.ID!!,it.collector,it.total,it.setting)
        }
    }


    private fun getRecollection(idCollector: Int, userState: String = "active") {
        CoroutineScope(Dispatchers.IO).launch{
            val allRecolection = AppDataBase.getInstance(this@ActivityRecolectionDetail).RecolectoresDao().getCollectorAndCollection(userState, idCollector)
                launch(Dispatchers.Main) {
                    collectionUpdate = listOf(allRecolection[0])
                    adapter = adpaterRvRecolection(allRecolection)
                        binding.rvRecolections.adapter = adapter
                        binding.rvRecolections.layoutManager = LinearLayoutManager(this@ActivityRecolectionDetail)
                }
        }
    }

}
