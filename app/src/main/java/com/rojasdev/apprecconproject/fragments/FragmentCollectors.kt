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
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.adapters.adapterRvCollectors
import com.rojasdev.apprecconproject.alert.alertCollection
import com.rojasdev.apprecconproject.alert.alertDeleteCollector
import com.rojasdev.apprecconproject.alert.alertMessage
import com.rojasdev.apprecconproject.controller.customSnackBar
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.data.entities.RecollectionEntity
import com.rojasdev.apprecconproject.databinding.FragmentCollectorsAndCollecionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentCollectors(
        var scroll:(String)-> Unit,
        var preferences:()-> Unit
    ) : Fragment() {

    private lateinit var adapter: adapterRvCollectors
    private var _binding: FragmentCollectorsAndCollecionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectorsAndCollecionBinding.inflate(inflater,container,false)

        binding.lyTotal.visibility = View.GONE

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
            val idCollectors = AppDataBase.getInstance((requireContext())).RecollectionDao().getFkIdCollectors()
            val collectors = AppDataBase.getInstance(requireContext()).RecolectoresDao().getAllRecolector()

            launch(Dispatchers.Main) {
                if (collectors.isNotEmpty()){
                    initRv(idCollectors,collectors)
                }else{
                    preferencesUpdate()
                }
            }
        }
    }

    private fun initRv(idCollectors: List<Long>, collectors: List<RecolectoresEntity>) {
        adapter = adapterRvCollectors(
            collectors,
            idCollectors,
            { item ->
                initDetailCollector(item) // Next Activity
            },
            {
                initAlertDelete(it) // Delete
            },
            {
                initAlertAddCollection(it) // Add collection
            }
        )

        binding.rvCollectors.adapter = adapter
        binding.rvCollectors.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initDetailCollector(item: RecolectoresEntity) {
        startActivity(Intent(
            requireContext(), ActivityRecolectionDetail::class.java
        ).putExtra("userId", item.id).putExtra("userName", item.name))
    }

    private fun initAlertDelete(it: RecolectoresEntity) {
        alertDeleteCollector(it.name){
            customSnackBar.showCustomSnackBar(requireView(),getString(R.string.deleteCollector))

            CoroutineScope(Dispatchers.IO).launch {
                AppDataBase.getInstance(requireContext()).RecolectoresDao().deleteCollectorId(it.id!!)
                launch { dates() }
            }

        }.show(parentFragmentManager,"dialog")
    }

    private fun initAlertAddCollection(it: RecolectoresEntity) {
        alertCollection(it){
            insertCollection(it)
        }.show(parentFragmentManager,"dialog")
    }

    private fun insertCollection(recollection: RecollectionEntity) {
        customSnackBar.showCustomSnackBar(binding.fragmentCollectors,getString(R.string.addCollectionFinish))

        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(requireContext()).RecollectionDao().addRecollection(recollection)
            launch { dates() }
        }
    }

    private fun preferencesUpdate(){
        CoroutineScope(Dispatchers.IO).launch{
            val idCollectors = AppDataBase.getInstance((requireContext())).RecollectionDao().getFkIdCollectors()
            launch(Dispatchers.Main) {
                if(idCollectors.isEmpty()){
                    alertMessage(
                        getString(R.string.txtMessageOne),
                        getString(R.string.txtMessageTwo),
                        getString(R.string.txtRecolectionStart),
                        getString(R.string.btnFinish),
                        getString(R.string.requireCollectors)
                    ){
                        if(it == "yes"){
                            preferences()
                            startActivity(Intent(requireContext(),ActivityMainModule::class.java))
                        }else{
                            preferences()
                            startActivity(Intent(requireContext(),ActivityMainModule::class.java))
                        }
                    }.show(parentFragmentManager,"dialog")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}