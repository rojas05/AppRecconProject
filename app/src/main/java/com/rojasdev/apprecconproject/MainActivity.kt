package com.rojasdev.apprecconproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    //private lateinit var database: AppDataBase
    //private lateinit var recolectorDao: RecolectoresDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
                setContentView(binding.root)

        //database = AppDataBase.getInstance(this)
        //recolectorDao = database.RecolectoresDao()

        binding.btnAdd.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAdd -> {
                var recolector: String
                if (TextUtils.isEmpty(binding.user.text.toString())) {
                    binding.user.error = "ingrese el nombre"
                    binding.user.requestFocus()
                    return
                } else {
                    recolector = binding.user.text.toString()
                    Toast.makeText(this, "Se agrago un dato", Toast.LENGTH_SHORT).show()
                }
                    val infoRecolector = RecolectoresEntity(null,recolector)
                        GlobalScope.launch {
                            AppDataBase.getInstance(this@MainActivity).RecolectoresDao().add(infoRecolector)
                        }
            }
        }
    }
}