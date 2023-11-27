package com.rojasdev.apprecconprojectPro.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconprojectPro.databinding.FragmentCollectorsAndCollecionBinding
import com.rojasdev.apprecconprojectPro.ActivityMainModule
import com.rojasdev.apprecconprojectPro.ActivityRecolectionDetail
import com.rojasdev.apprecconprojectPro.adapters.adapterRvCollectors
import com.rojasdev.apprecconprojectPro.alert.alertCollection
import com.rojasdev.apprecconprojectPro.alert.alertDeleteCollector
import com.rojasdev.apprecconprojectPro.data.dataBase.AppDataBase
import com.rojasdev.apprecconprojectPro.data.entities.RecolectoresEntity
import com.rojasdev.apprecconprojectPro.data.entities.RecollectionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentCollectors(
    var scroll:(String)-> Unit) : Fragment() {
    private var _binding: FragmentCollectorsAndCollecionBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: adapterRvCollectors

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectorsAndCollecionBinding.inflate(inflater,container,false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                startActivity(Intent(requireContext(),ActivityMainModule::class.java))
            }
        })

        CoroutineScope(Dispatchers.IO).launch {
            dates()
        }

        com.rojasdev.apprecconprojectPro.controller.scrolling.scrolling(binding.rvCollectors){
            scroll(it)
        }

        return binding.root
    }

    private suspend fun dates(){
        CoroutineScope(Dispatchers.IO).launch{
            val idCollectors = AppDataBase.getInstance((requireContext())).RecollectionDao().getfKIdCollectors()
            val ingresosL = AppDataBase.getInstance(requireContext()).RecolectoresDao().getAllRecolector()
            launch(Dispatchers.Main) {
                adapter = adapterRvCollectors(
                    ingresosL,
                    idCollectors,
                    { item ->
                        //pasar a detalle recolector
                        val id: Int? = item.id
                        if (id != null) {
                            CoroutineScope(Dispatchers.IO).launch {
                                val getId = AppDataBase.getInstance(requireContext()).RecollectionDao().getCollectionIdCollector(id)
                                launch(Dispatchers.Main) {
                                    if(getId.isNotEmpty()) {
                                        startActivity(Intent(
                                            requireContext(), ActivityRecolectionDetail::class.java
                                         ).putExtra("userId", item.id).putExtra("userName", item.name)
                                        )
                                    } else {
                                            Toast.makeText(requireContext(), "No hay datos", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    },
                    {
                        //delete
                        initAlertDelete(it)
                    },
                    {
                        //add collection
                        InitAlertAddCollection(it)
                    }
                )

                binding.rvCollectors.adapter = adapter
                binding.rvCollectors.layoutManager = LinearLayoutManager(requireContext())

            }
        }
    }

    private fun initAlertDelete(it: RecolectoresEntity) {
        alertDeleteCollector(
            it.name
        ){
            CoroutineScope(Dispatchers.IO).launch {
                AppDataBase.getInstance(requireContext()).RecolectoresDao().deleteCollectorId(it.id!!)
                launch {
                    dates()
                }
            }
        }.show(parentFragmentManager,"dialog")
    }

    private fun InitAlertAddCollection(it: RecolectoresEntity) {
        alertCollection(it){
            insertCollection(it)
        }.show(parentFragmentManager,"dialog")
    }

    private fun insertCollection(recollection: RecollectionEntity) {
        Toast.makeText(requireContext(), "Apuntado", Toast.LENGTH_SHORT).show()
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(requireContext()).RecollectionDao().addRecoleccion(recollection)
            launch {
                dates()
            }
        }
    }

}