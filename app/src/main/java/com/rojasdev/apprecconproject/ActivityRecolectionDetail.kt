package com.rojasdev.apprecconproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.adapters.adpaterRvRecolection
import com.rojasdev.apprecconproject.alert.alertCollectionUpdate
import com.rojasdev.apprecconproject.alert.alertUpdateNameCollector
import com.rojasdev.apprecconproject.controller.customSnackbar
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
    private var idCollector: Int? = null
    private var userName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRecolectionDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Recibir parametros
        idCollector = intent.getIntExtra("userId", 0)
        userName = intent.getStringExtra("userName")

        title = userName

        getRecollection(idCollector!!)

    }

    private fun getRecollection(idCollector: Int) {
        CoroutineScope(Dispatchers.IO).launch{
            val collection= AppDataBase.getInstance(this@ActivityRecolectionDetail).RecolectoresDao().getCollectorAndCollection("active",idCollector)

            val totalRecolection = AppDataBase.getInstance(this@ActivityRecolectionDetail).RecolectoresDao().getCollectorAndCollectionTotal(idCollector)
            launch(Dispatchers.Main) {
                collectionUpdate = listOf(collection[0])
                collectionTotal = listOf(totalRecolection[0])
                adapter = adpaterRvRecolection(collection) {
                    // Update Collection
                    alertUpdateRecollection(it, idCollector)
                }
                price.priceSplit(totalRecolection[0].price_total.toInt()){
                    binding.tvTotal.text = "${getString(R.string.totalPrinceCancel)}\n${it}"
                }

                binding.tvTitle.text = "${getString(R.string.recolection)}\n${totalRecolection[0].kg_collection.toFloat()} Kg"
                binding.rvRecolections.adapter = adapter
                binding.rvRecolections.layoutManager = LinearLayoutManager(this@ActivityRecolectionDetail)
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
            AppDataBase.getInstance(this@ActivityRecolectionDetail).RecollectionDao().updateCollection(it.ID!!,it.date,it.collector,it.total,it.setting)
            launch(Dispatchers.Main){
                getRecollection(idCollector)
                customSnackbar.showCustomSnackbar(binding.rvRecolections,getString(R.string.updateFinish))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.editName -> showAlertEditName()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAlertEditName() {
        alertUpdateNameCollector(
            userName.toString(),
            idCollector!!
        ){
            CoroutineScope(Dispatchers.IO).launch {
                AppDataBase.getInstance(this@ActivityRecolectionDetail).RecolectoresDao().updateCollectorName(it.id!!,it.name)
                launch(Dispatchers.Main) {
                    customSnackbar.showCustomSnackbar(binding.rvRecolections,getString(R.string.editNameRedy))
                }
            }
        }.show(supportFragmentManager,"dialog")
    }
}
