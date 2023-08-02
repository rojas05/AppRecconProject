package com.rojasdev.apprecconproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.adapters.adapterItemDate
import com.rojasdev.apprecconproject.controller.animatedAlert
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
    private lateinit var colletion: List<allCollecionAndCollector>
    private var _binding: FragmentInformeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInformeBinding.inflate(inflater,container,false)

        requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,object : OnBackPressedCallback(true
            ){
                override fun handleOnBackPressed() {

                }
            })

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = getDate(year, month, dayOfMonth)
            binding.tvShowDates.text = "fecha: $selectedDate"
            showAllRecolecion(selectedDate)
        }

        animatedAlert.animatedCv(binding.cardView)

        return binding.root
    }

    private fun showAllRecolecion(selectedDate: String) {
        CoroutineScope(Dispatchers.IO).launch{
            val showAll = AppDataBase.getInstance(requireContext()).RecolectoresDao().getAllCollectorAndCollection(selectedDate)
            launch(Dispatchers.Main) {
                colletion = ArrayList(showAll)
                adapter = adapterItemDate(showAll)

                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
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

}