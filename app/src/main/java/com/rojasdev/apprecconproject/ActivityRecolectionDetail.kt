package com.rojasdev.apprecconproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.adapters.adapterRvCollectors
import com.rojasdev.apprecconproject.adapters.adpaterRvRecolection
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.data.entities.RecollectionEntity
import com.rojasdev.apprecconproject.databinding.ActivityRecolectionDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityRecolectionDetail : AppCompatActivity() {

    lateinit var binding: ActivityRecolectionDetailBinding
        private lateinit var adapter: adpaterRvRecolection

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRecolectionDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val idCollector = intent.getIntExtra("userId", 0)
        val userName = intent.getStringExtra("userName")

        title = userName

        getRecollection(idCollector)

        binding.button

    }

    private fun getRecollection(idCollector: Int, userState: String = "active") {
        CoroutineScope(Dispatchers.IO).launch{
            val allRecolection = AppDataBase.getInstance(this@ActivityRecolectionDetail).RecolectoresDao().getCollectorAndCollection(userState, idCollector)
                launch(Dispatchers.Main) {
                    adapter = adpaterRvRecolection(allRecolection)
                        binding.rvRecolections.adapter = adapter
                        binding.rvRecolections.layoutManager = LinearLayoutManager(this@ActivityRecolectionDetail)
                }
        }
    }

    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
          val checked: Boolean = view.isChecked
            when (view.id) {
                R.id.yesFeendingCheckBox -> {
                    if (checked) {
                        Toast.makeText(this, "presiono si", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "no presiono", Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.notFeendingCheckBox -> {
                    if (checked) {
                        Toast.makeText(this, "presiono si de no", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "presiono no de si", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}