package com.rojasdev.apprecconproject.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.data.entities.ReportHistoryEntity
import com.rojasdev.apprecconproject.viewHolders.viewHolderReportHistory

class adapterRvReportHistory(
        private var itemHistory: List<ReportHistoryEntity>
    ): RecyclerView.Adapter<viewHolderReportHistory>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderReportHistory {
        return viewHolderReportHistory(LayoutInflater.from(parent.context).inflate(R.layout.item_rv_report_history, parent, false))
    }

    override fun getItemCount(): Int = itemHistory.size

    override fun onBindViewHolder(holder: viewHolderReportHistory, position: Int) {
        val item = itemHistory[position]
        holder.render(item)
    }

}