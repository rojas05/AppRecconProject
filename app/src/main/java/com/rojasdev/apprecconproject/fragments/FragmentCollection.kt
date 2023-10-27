package com.rojasdev.apprecconproject.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.ActivityInformes
import com.rojasdev.apprecconproject.ActivityMainModule
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.adapters.adapterRvCollectionTotal
import com.rojasdev.apprecconproject.alert.alertCancelCollection
import com.rojasdev.apprecconproject.alert.alertMessage
import com.rojasdev.apprecconproject.controller.customSnackBar
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.controller.scrolling
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.dataModel.collecionTotalCollector
import com.rojasdev.apprecconproject.databinding.FragmentCollectorsAndCollecionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentCollection(
    var scroll:(String)-> Unit,
    var preferences:()-> Unit) : Fragment()
{
    private var _binding: FragmentCollectorsAndCollecionBinding? = null
    private lateinit var adapter: adapterRvCollectionTotal
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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

        getTotalCollection()

        return binding.root
    }

    private fun totalCollectionCollector(){
        CoroutineScope(Dispatchers.IO).launch{
            val idCollectors = AppDataBase.getInstance((requireContext())).RecolectoresDao().getIDCollectors()
            launch(Dispatchers.Main) {
                val collector = mutableListOf<collecionTotalCollector>()
                for(item in idCollectors){
                    val collectionTotal = AppDataBase.getInstance((requireContext())).RecolectoresDao().getCollectorAndCollectionTotal(item.toInt())
                    if(collectionTotal[0].name_recolector != null){
                        collector.add(collectionTotal[0])
                    }
                }
                dates(collector)
            }
        }
    }

    private fun dates(total:List<collecionTotalCollector>) {
    adapter = adapterRvCollectionTotal(total) {
        initCancelCollection(it)
    }
        binding.rvCollectors.adapter = adapter
        binding.rvCollectors.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initCancelCollection(collectionTotal: collecionTotalCollector) {
        CoroutineScope(Dispatchers.IO).launch{
            val collection = AppDataBase.getInstance((requireContext())).RecolectoresDao().getCollectorAndCollection("active",collectionTotal.PK_ID_Recolector)

            launch(Dispatchers.Main) {
                alertCancelCollection(listOf(collectionTotal), collection) {
                    updateCollection(it)
                }.show(parentFragmentManager,"dialog")
            }
        }
    }

    private fun updateCollection(idUpdate: Int) {
        CoroutineScope(Dispatchers.IO).launch{
            val dataBase = AppDataBase.getInstance(requireContext())

            dataBase.RecolectoresDao().updateCollectorState(idUpdate)
            dataBase.RecollectionDao().updateCollectionState(idUpdate)

            launch(Dispatchers.Main) {
                customSnackBar.showCustomSnackBar(requireView(),getString(R.string.collectionCanceled))
                totalCollectionCollector()
                preferencesUpdate()
            }
        }
    }

    private fun preferencesUpdate(){
        CoroutineScope(Dispatchers.IO).launch{
            val idCollectors = AppDataBase.getInstance((requireContext())).RecollectionDao().getFkIdCollectors()
            launch(Dispatchers.Main) {
                if(idCollectors.isEmpty()){
                    alertMessage(
                        getString(R.string.txtNewReport),
                        getString(R.string.txtCalendar),
                        getString(R.string.txtGoReport),
                        getString(R.string.txtReturnMenu),
                        getString(R.string.txtRecolectionFull)
                    ){
                        if(it == "yes"){
                            preferences()
                            startActivity(Intent(requireContext(),ActivityInformes::class.java))
                        }else{
                            preferences()
                            startActivity(Intent(requireContext(),ActivityMainModule::class.java))
                        }
                    }.show(parentFragmentManager,"dialog")
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getTotalCollection(){
        CoroutineScope(Dispatchers.IO).launch{
            val collectionTotal = AppDataBase.getInstance((requireContext())).SettingDao().getTotalCollectionActive()
            launch(Dispatchers.Main) {
                if(collectionTotal.isNotEmpty()){
                    binding.lyTotal.visibility = View.VISIBLE
                    binding.tvCollection.text = "Total recolectado\n ${collectionTotal[0].cantidad.toFloat()}Kg"

                    price.priceSplit(collectionTotal[0].total.toInt()){
                        binding.tvTotal.text = "Total a pagar\n $it"
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}