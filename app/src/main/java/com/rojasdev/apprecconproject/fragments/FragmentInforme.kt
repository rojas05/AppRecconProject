package com.rojasdev.apprecconproject.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.ActivityMainModule
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.adapters.adapterItemDate
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.customSnackbar
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.dataModel.allCollecionAndCollector
import com.rojasdev.apprecconproject.data.dataModel.collecionTotalCollector
import com.rojasdev.apprecconproject.data.dataModel.collectorCollection
import com.rojasdev.apprecconproject.databinding.FragmentInformeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Year
import java.util.Calendar
import java.util.Locale

class FragmentInforme : Fragment() {

    private lateinit var adapter: adapterItemDate
    private var _binding: FragmentInformeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInformeBinding.inflate(inflater,container,false)

        requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,object : OnBackPressedCallback(true
            ){
                override fun handleOnBackPressed() { startActivity(Intent(requireContext(),ActivityMainModule::class.java)) }
            })

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = getDate(year, month, dayOfMonth)
            showAllRecolecion(selectedDate)
        }

         // Get phone date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.set(year, month, dayOfMonth)
        val dateFormat = "EEEE, MMMM dd 'del' yyyy"
        val format = SimpleDateFormat(dateFormat, Locale("es", "ES"))
        val date = format.format(calendar.time)
         // Show current day data
        showAllRecolecion(date)

        animatedAlert.animatedCv(binding.cardView)

        return binding.root
    }

    private fun showAllRecolecion(selectedDate: String) {
        CoroutineScope(Dispatchers.IO).launch{
            val getAllID = AppDataBase.getInstance(requireContext()).RecolectoresDao().getAll()
            val getTotalKg = AppDataBase.getInstance(requireContext()).RecolectoresDao().getTotalKgDate("${selectedDate}%")
            launch(Dispatchers.Main) {
                val showAll = mutableListOf<allCollecionAndCollector>()
                for (item in getAllID){
                    val query = AppDataBase.getInstance(requireContext()).RecolectoresDao().getAllCollectorAndCollectionId("${selectedDate}%",item.toInt())
                    if(query[0].name_recolector != null){
                        if(query.isNotEmpty()) showAll.add(query[0])
                    }
                }

                binding.tvShowDates.text = "Total Recolectado: ${getTotalKg[0].Cantidad} Kg"

                if (getTotalKg[0].Estado == "active"){
                    price.priceSplit(getTotalKg[0].result.toInt()){
                        binding.tvShowPay.text = "Total a Pagar: ${it}"
                    }
                } else {
                    price.priceSplit(getTotalKg[0].result.toInt()){
                        binding.tvShowPay.text = "Total Pagado: ${it}"
                    }
                }

                if(showAll.isEmpty()) {
                    //customSnackbar.showCustomSnackbar(requireView(),"No hay datos de ese dia")
                    binding.recyclerView.visibility = View.GONE
                    binding.userInfo.visibility = View.VISIBLE
                }else{

                    binding.nestedScrollView.smoothScrollTo(0, 744)

                    adapter = adapterItemDate(showAll)
                    binding.recyclerView.adapter = adapter
                    binding.userInfo.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                }
            }
        }
    }

    private fun getDate(year: Int, month: Int, dayOfMonth: Int): String {
        val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
        val dateFormat = "EEEE, MMMM dd 'del' yyyy"
        val format = SimpleDateFormat(dateFormat, Locale("es", "ES"))
        return format.format(calendar.time)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}