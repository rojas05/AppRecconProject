package com.rojasdev.apprecconproject.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.ActivityMainModule
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.adapters.adapterItemDate
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.controller.textToSpeech
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

class FragmentReport : Fragment() {

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
        binding.fbSpeech.visibility = View.GONE

        monthSelected()

        return binding.root
    }

    private fun initCalendar(it:Triple<Int,Int,String>) {
        CoroutineScope(Dispatchers.IO).launch {
            val list = AppDataBase.getInstance(requireContext()).RecollectionDao().getDateCollection()
            launch(Dispatchers.Main) {
                val listModification = list.map { it.dropLast(9) }
                val month = getDaysMonth(it.first,it.second)

                adapterDates = adapter(month,listModification){
                    showAllRecollection(it)
                }

                binding.rvDate.adapter = adapterDates
                binding.rvDate.layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showAllRecollection(selectedDate: String) {
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

                binding.tvShowDates.text = "${getString(R.string.tvRecolection)} \n ${getTotalKg[0].Cantidad.toFloat()} Kg"
                binding.tvShowDates.visibility = View.VISIBLE

                if (getTotalKg[0].Estado == "active"){
                    price.priceSplit(getTotalKg[0].result.toInt()){
                        binding.tvShowPay.text = "${getString(R.string.tvPriceTotal)} \n $it"
                        binding.tvShowPay.visibility = View.VISIBLE
                    }
                } else {
                    price.priceSplit(getTotalKg[0].result.toInt()){
                        binding.tvShowPay.text = "${getString(R.string.totalPrince)}: \n $it"
                        binding.tvShowPay.visibility = View.VISIBLE
                    }
                }

                if(showAll.isEmpty()) {
                    binding.recyclerView.visibility = View.GONE
                    binding.userInfo.visibility = View.VISIBLE // Show not Data
                }else{
                    binding.fbSpeech.visibility = View.VISIBLE
                    binding.fbSpeech.isClickable = true
                    binding.fbSpeech.setOnClickListener {
                        speechInformed(selectedDate,getTotalKg[0].Cantidad,getTotalKg[0].result.toInt())
                    }
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

    private fun speechInformed(selectedDate: String,kg: Double, price : Int) {

        val formatDateOriginal = SimpleDateFormat("yyyy-MM-dd", Locale("es", "CO"))
        val formatDate = SimpleDateFormat("EEEE, dd MMMM 'del' yyyy", Locale("es", "CO"))
        val date = formatDateOriginal.parse(selectedDate)
        val dateFormat = date?.let { formatDate.format(it) }

        CoroutineScope(Dispatchers.IO).launch {
            textToSpeech().start(
                requireContext(),
                "${getString(R.string.collection)} del $dateFormat \n" +
                        "${getString(R.string.recolection)} $kg Kilogramos\n" +
                        "${getString(R.string.valorTotal)} $price COP"
            ){}
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun getDaysMonth(year: Int, month: Int): List<List<dataModelDay>> {
        val diasDelMes = mutableListOf<List<dataModelDay>>()
        val calendar = Calendar.getInstance()
            calendar.set(year, month - 1, 1)
        val ultimoDiaDelMes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        var week = mutableListOf<dataModelDay>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("es", "ES"))
        val formatDayWeek = SimpleDateFormat("EEEE", Locale("es", "ES"))

        for (dia in 1..ultimoDiaDelMes) {
            calendar.set(Calendar.DAY_OF_MONTH, dia)
            val fechaActual = calendar.time
            val diaDeLaSemana = formatDayWeek.format(fechaActual)

            val day = dataModelDay(dateFormat.format(fechaActual),dia.toString(),diaDeLaSemana)

            week.add(day)

            if (diaDeLaSemana == "domingo" || dia == ultimoDiaDelMes) {
                diasDelMes.add(week)
                week = mutableListOf()
            }
        }

        return diasDelMes
    }

    @SuppressLint("SetTextI18n")
    fun monthSelected(){
        val calendar = Calendar.getInstance()
        val formatMontNum = SimpleDateFormat("MM", Locale.getDefault())
        val formatMont = SimpleDateFormat("MMMM", Locale.getDefault())

        val monthNum = formatMontNum.format(calendar.time)
        val monthString = formatMont.format(calendar.time)

        val year = calendar.get(Calendar.YEAR)

        var previous = getPreviousMonth("$year-$monthNum")
        var next = getNextMonth("$year-$monthNum")

        var monthCalendar = Triple(year,monthNum.toInt(),monthString)

        binding.tvMonthPrevious.text = previous.first
        binding.tvMonthNext.text = next.first
        binding.tvMonth.text = monthCalendar.third
        binding.tvTitleCalendar.text = "${getString(R.string.calendarCollection)}\n${monthCalendar.first}"

        binding.tvMonthPrevious.setOnClickListener {
            monthCalendar = Triple(previous.second,previous.third,previous.first)
            previous = getPreviousMonth("${monthCalendar.first}-${monthCalendar.second}")
            next = getNextMonth("${monthCalendar.first}-${monthCalendar.second}")
            binding.tvMonthPrevious.text = previous.first
            binding.tvMonthNext.text = next.first
            binding.tvMonth.text = monthCalendar.third
            binding.tvTitleCalendar.text = "${getString(R.string.calendarCollection)}\n${monthCalendar.first}"
            initCalendar(monthCalendar)
        }

        binding.tvMonthNext.setOnClickListener {
            monthCalendar = Triple(next.second,next.third,next.first)
            next = getNextMonth("${monthCalendar.first}-${monthCalendar.second}")
            previous = getPreviousMonth("${monthCalendar.first}-${monthCalendar.second}")
            binding.tvMonthPrevious.text = previous.first
            binding.tvMonthNext.text = next.first
            binding.tvMonth.text = monthCalendar.third
            binding.tvTitleCalendar.text = "${getString(R.string.calendarCollection)}\n${monthCalendar.first}"
            initCalendar(monthCalendar)
        }

        initCalendar(monthCalendar)
    }

    @SuppressLint("SimpleDateFormat")
    fun getPreviousMonth(fecha: String): Triple<String,Int,Int> {
        val dateFormat = SimpleDateFormat("yyyy-MM")
        val specificDate = dateFormat.parse(fecha)
        val calendar: Calendar = GregorianCalendar()
            calendar.time = specificDate!!
            calendar.add(Calendar.MONTH, -1)
        val monthFormat = SimpleDateFormat("MMMM", Locale("es", "ES"))
        val fullMonth = monthFormat.format(calendar.time)
        val year = calendar.get(Calendar.YEAR)
        val monthInt = calendar.get(Calendar.MONTH)
        return Triple(fullMonth, year, monthInt + 1)
    }

    @SuppressLint("SimpleDateFormat")
    fun getNextMonth(fecha: String): Triple<String,Int,Int> {
        val dayFormat = SimpleDateFormat("yyyy-MM")
        val specificDate = dayFormat.parse(fecha)
        val calendar: Calendar = GregorianCalendar()
            calendar.time = specificDate!!
            calendar.add(Calendar.MONTH, + 1)
        val monthFormat = SimpleDateFormat("MMMM", Locale("es", "ES"))
        val fullMonth = monthFormat.format(calendar.time)
        val year = calendar.get(Calendar.YEAR)
        val monthInt = calendar.get(Calendar.MONTH)
        return Triple(fullMonth, year, monthInt+ 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}