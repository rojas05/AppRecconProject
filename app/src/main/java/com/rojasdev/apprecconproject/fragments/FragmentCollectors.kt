package com.rojasdev.apprecconproject.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.ActivityMainModule
import com.rojasdev.apprecconproject.ActivityRecolectionDetail
import com.rojasdev.apprecconproject.adapters.adapterRvCollectors
import com.rojasdev.apprecconproject.alert.alertCollection
import com.rojasdev.apprecconproject.alert.alertDeleteCollector
import com.rojasdev.apprecconproject.controller.customSnackbar
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.data.entities.RecollectionEntity
import com.rojasdev.apprecconproject.databinding.FragmentCollectorsAndCollecionBinding
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

        com.rojasdev.apprecconproject.controller.scrolling.scrolling(binding.rvCollectors){
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
                        initDetailCollector(item) // Next Activity
                    },
                    {
                        initAlertDelete(it) // Delete
                    },
                    {
                        InitAlertAddCollection(it) // Add Recolection
                    }
                )

                binding.rvCollectors.adapter = adapter
                binding.rvCollectors.layoutManager = LinearLayoutManager(requireContext())

            }
        }
    }

    private fun initDetailCollector(item: RecolectoresEntity) {
        val id:Int? = item.id
        if (id != null) {
            CoroutineScope(Dispatchers.IO).launch {
                val getId = AppDataBase.getInstance(requireContext()).RecollectionDao().getCollectionIdCollector(id)
                launch(Dispatchers.Main) {
                    if(getId.isNotEmpty()) {
                        startActivity(Intent(
                                requireContext(), ActivityRecolectionDetail::class.java
                            ).putExtra("userId", item.id).putExtra("userName", item.name) // Pasar parametros
                        )
                    }
                }
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
        customSnackbar.showCustomSnackbar(binding.fragmentCollectors,"Recoleccion guardada")
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(requireContext()).RecollectionDao().addRecoleccion(recollection)
            launch {
                dates()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}