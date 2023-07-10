package com.rojasdev.apprecconproject.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.ActivityMainModule
import com.rojasdev.apprecconproject.adapters.adapterRvCollectors
import com.rojasdev.apprecconproject.alert.alertCollection
import com.rojasdev.apprecconproject.data.dao.RecollectionDao
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.data.entities.RecollectionEntity
import com.rojasdev.apprecconproject.databinding.ActivityMainModuleBinding
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
            val idCollectors = AppDataBase.getInstance((requireContext())).RecollectionDao().getfKIdCollectorS()
            val ingresosL = AppDataBase.getInstance(requireContext()).RecolectoresDao().getAllRecolector()
            launch(Dispatchers.Main) {
                adapter = adapterRvCollectors(
                    ingresosL,
                    idCollectors,
                    {
                        //pasar a detalle recolector
                    },
                    {
                        //delete
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