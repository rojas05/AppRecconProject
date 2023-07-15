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
import com.rojasdev.apprecconproject.adapters.adapterRvColleccionTotal
import com.rojasdev.apprecconproject.adapters.adapterRvCollectors
import com.rojasdev.apprecconproject.controller.scrolling
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.dataModel.collecionTotalCollector
import com.rojasdev.apprecconproject.databinding.FragmentCollectorsAndCollecionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FragmentCollecion(
    var scroll:(String)-> Unit) : Fragment() {
    private var _binding: FragmentCollectorsAndCollecionBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: adapterRvColleccionTotal

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectorsAndCollecionBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    startActivity(Intent(requireContext(), ActivityMainModule::class.java))
                }
            })

        totalCollectionCollector()

        scrolling.scrolling(binding.rvCollectors) {
            scroll(it)
        }


        return binding.root
    }

    fun totalCollectionCollector(){
        CoroutineScope(Dispatchers.IO).launch{
            val idCollectors = AppDataBase.getInstance((requireContext())).RecollectionDao().getfKIdCollectorS()
            launch(Dispatchers.Main) {
                val collector = mutableListOf<collecionTotalCollector>()
                for(item in idCollectors){
                    val collectionTotal = AppDataBase.getInstance((requireContext())).RecolectoresDao().getCollectorAndCollectionTotal(item.toInt())
                    collector.add(collectionTotal[0])
                }
                dates(collector)
            }
        }
    }

    private fun dates(total:List<collecionTotalCollector>) {
        CoroutineScope(Dispatchers.IO).launch{
            launch(Dispatchers.Main) {
                adapter = adapterRvColleccionTotal(total)
                {

                }
                binding.rvCollectors.adapter = adapter
                binding.rvCollectors.layoutManager = LinearLayoutManager(requireContext())

            }
        }
    }

}