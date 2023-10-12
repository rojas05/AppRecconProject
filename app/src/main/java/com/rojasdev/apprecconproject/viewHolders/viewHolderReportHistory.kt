package com.rojasdev.apprecconproject.viewHolders

import android.annotation.SuppressLint
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.data.entities.ReportHistoryEntity
import com.rojasdev.apprecconproject.databinding.ItemRvReportHistoryBinding
import java.text.SimpleDateFormat
import java.util.Locale

class viewHolderReportHistory(var view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemRvReportHistoryBinding.bind(view)

    @SuppressLint("SetTextI18n")
    fun render(itemHistory: ReportHistoryEntity){

        binding.cvReportHistory.animation = AnimationUtils.loadAnimation(view.context, R.anim.recycler_transition)

        val getDate = itemHistory.date
        val formatDateOriginal = SimpleDateFormat("yyyy-MM-dd", Locale("es", "CO"))
        val format = SimpleDateFormat("EEEE dd MMMM", Locale("es", "CO"))
        val date = formatDateOriginal.parse(getDate)
        val dateFormat = format.format(date!!)


        binding.tvNamePdf.text = itemHistory.name
        binding.tvReportType.text = "Tipo de Informe: ${itemHistory.reportType}"
        binding.tvDate.text = "Fecha Generada: $dateFormat"
    }

}