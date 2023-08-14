package com.rojasdev.apprecconproject.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.ActivityMainModule
import com.rojasdev.apprecconproject.adapters.adapterItemDate
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.dataModel.allCollecionAndCollector
import com.rojasdev.apprecconproject.databinding.FragmentInformeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
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

         // Obtene la fecha del calendario
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = getDate(year, month, dayOfMonth)
            showAllRecolecion(selectedDate)
        }

         // Get phone date
        val calendar = Calendar.getInstance().time
        val format = SimpleDateFormat("EEEE, MMMM dd 'del' yyyy", Locale("es", "ES"))
        val date = format.format(calendar)
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
                    binding.recyclerView.visibility = View.GONE
                    binding.userInfo.visibility = View.VISIBLE // Mostrar que no hay datos
                }else{

                    binding.nestedScrollView.smoothScrollTo(0, 844) // Auto Scroll si hay datos

                    adapter = adapterItemDate(showAll)
                    binding.recyclerView.adapter = adapter
                    binding.userInfo.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                }
            }
        }
    }

     // Obtengo el formato de fecha como la BD
    private fun getDate(year: Int, month: Int, dayOfMonth: Int): String {
        val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
        val dateFormat = "EEEE, MMMM dd 'del' yyyy"
        val format = SimpleDateFormat(dateFormat, Locale("es", "ES"))
        return format.format(calendar.time)
    }

     // Liberar la referencia de la vista y evitar posibles fugas de memoria.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}