package com.rojasdev.apprecconproject.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.ActivityMainModule
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.adapters.adapterItemDate
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.customCalendar.adapter
import com.rojasdev.apprecconproject.customCalendar.dataModelDay
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.dataModel.allCollecionAndCollector
import com.rojasdev.apprecconproject.databinding.FragmentInformeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

@Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
class FragmentInforme : Fragment() {

    private lateinit var adapter: adapterItemDate
    private lateinit var adapterDates: adapter
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

        binding.tvShowPay.visibility = View.GONE


        monthSelected()

        return binding.root
    }

    private fun initCalendar(it:Triple<Int,Int,String>) {
        CoroutineScope(Dispatchers.IO).launch {
            val list = AppDataBase.getInstance(requireContext()).RecollectionDao().getDateCollection()
            launch(Dispatchers.Main) {
                val listaModificada = list.map { it.dropLast(9) }
                val month = obtenerDiasDelMes(it.first,it.second)

                adapterDates = adapter(month,listaModificada){
                    showAllRecolecion(it)
                }

                binding.rvDate.adapter = adapterDates
                binding.rvDate.layoutManager = LinearLayoutManager(requireContext())
            }
        }
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

                binding.tvShowDates.text = "Total Recolectado:\n${getTotalKg[0].Cantidad.toFloat()} Kg"
                binding.tvShowDates.visibility = View.VISIBLE

                if (getTotalKg[0].Estado == "active"){
                    price.priceSplit(getTotalKg[0].result.toInt()){
                        binding.tvShowPay.text = "Total a Pagar:\n${it}"
                        binding.tvShowPay.visibility = View.VISIBLE
                    }
                } else {
                    price.priceSplit(getTotalKg[0].result.toInt()){
                        binding.tvShowPay.text = "Total Pagado:\n${it}"
                        binding.tvShowPay.visibility = View.VISIBLE
                    }
                }

                if(showAll.isEmpty()) {
                    binding.recyclerView.visibility = View.GONE
                    binding.userInfo.visibility = View.VISIBLE // Mostrar que no hay datos
                }else{

                    binding.nestedScrollView.smoothScrollTo(0, 900) // Auto Scroll si hay datos

                    adapter = adapterItemDate(showAll)
                    binding.recyclerView.adapter = adapter
                    binding.userInfo.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                }
            }
        }
    }

    fun obtenerDiasDelMes(year: Int, month: Int): List<List<dataModelDay>> {
        val diasDelMes = mutableListOf<List<dataModelDay>>()
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1)
        val ultimoDiaDelMes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        var semana = mutableListOf<dataModelDay>()
        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale("es", "ES"))
        val formatoDiaSemana = SimpleDateFormat("EEEE", Locale("es", "ES"))

        for (dia in 1..ultimoDiaDelMes) {
            calendar.set(Calendar.DAY_OF_MONTH, dia)
            val fechaActual = calendar.time
            val diaDeLaSemana = formatoDiaSemana.format(fechaActual)

            val day = dataModelDay("${formatoFecha.format(fechaActual)}",dia.toString(),diaDeLaSemana)

            semana.add(day)

            if (diaDeLaSemana == "domingo" || dia == ultimoDiaDelMes) {
                diasDelMes.add(semana)
                semana = mutableListOf()
            }
        }

        return diasDelMes
    }

    fun monthSelected(){
        val calendar = Calendar.getInstance()
        val formatMontNum = SimpleDateFormat("MM", Locale.getDefault())
        val formatMont = SimpleDateFormat("MMMM", Locale.getDefault())

        val monthNum = formatMontNum.format(calendar.time)
        val monthString = formatMont.format(calendar.time)

        val year = calendar.get(Calendar.YEAR)

        var previus = obtenerMesAnterior("$year-$monthNum")
        var next = obtenerMesSiguiente("$year-$monthNum")

        var monthCalendar = Triple(year,monthNum.toInt(),monthString)

        binding.tvMonthPrevious.text = previus.first
        binding.tvMonthNext.text = next.first
        binding.tvMonth.text = monthCalendar.third
        binding.tvTitleCalendar.text = "${getString(R.string.calendarCollection)}\n${monthCalendar.first}"

        binding.tvMonthPrevious.setOnClickListener {
            monthCalendar = Triple(previus.second,previus.third,previus.first)
            previus = obtenerMesAnterior("${monthCalendar.first}-${monthCalendar.second}")
            next = obtenerMesSiguiente("${monthCalendar.first}-${monthCalendar.second}")
            binding.tvMonthPrevious.text = previus.first
            binding.tvMonthNext.text = next.first
            binding.tvMonth.text = monthCalendar.third
            binding.tvTitleCalendar.text = "${getString(R.string.calendarCollection)}\n${monthCalendar.first}"
            initCalendar(monthCalendar)
        }

        binding.tvMonthNext.setOnClickListener {
            monthCalendar = Triple(next.second,next.third,next.first)
            next = obtenerMesSiguiente("${monthCalendar.first}-${monthCalendar.second}")
            previus = obtenerMesAnterior("${monthCalendar.first}-${monthCalendar.second}")
            binding.tvMonthPrevious.text = previus.first
            binding.tvMonthNext.text = next.first
            binding.tvMonth.text = monthCalendar.third
            binding.tvTitleCalendar.text = "${getString(R.string.calendarCollection)}\n${monthCalendar.first}"
            initCalendar(monthCalendar)
        }

        initCalendar(monthCalendar)
    }

    fun obtenerMesAnterior(fecha: String): Triple<String,Int,Int> {
        val formatoFecha = SimpleDateFormat("yyyy-MM")
        val fechaEspecifica = formatoFecha.parse(fecha)
        val calendario: Calendar = GregorianCalendar()
        calendario.time = fechaEspecifica
        calendario.add(Calendar.MONTH, -1)
        val formatoMes = SimpleDateFormat("MMMM", Locale("es", "ES"))
        val mesCompleto = formatoMes.format(calendario.time)
        val anio = calendario.get(Calendar.YEAR)
        val monthInt = calendario.get(Calendar.MONTH)
        return Triple(mesCompleto, anio, monthInt+1)
    }

    fun obtenerMesSiguiente(fecha: String): Triple<String,Int,Int> {
        val formatoFecha = SimpleDateFormat("yyyy-MM")
        val fechaEspecifica = formatoFecha.parse(fecha)
        val calendario: Calendar = GregorianCalendar()
        calendario.time = fechaEspecifica
        calendario.add(Calendar.MONTH, +1)
        val formatoMes = SimpleDateFormat("MMMM", Locale("es", "ES"))
        val mesCompleto = formatoMes.format(calendario.time)
        val anio = calendario.get(Calendar.YEAR)
        val monthInt = calendario.get(Calendar.MONTH)
        return Triple(mesCompleto, anio, monthInt+1)
    }



    // Obtengo el formato de fecha como la BD
    private fun getDate(year: Int, month: Int, dayOfMonth: Int): String {
        val calendar = Calendar.getInstance()

        calendar.set(year, month, dayOfMonth)
        val dateFormat = "yyyy-MM-dd"
        val format = SimpleDateFormat(dateFormat, Locale("es", "ES"))
        return format.format(calendar.time)
    }

     // Liberar la referencia de la vista y evitar posibles fugas de memoria.
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}