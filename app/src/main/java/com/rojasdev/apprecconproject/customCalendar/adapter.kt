package com.rojasdev.apprecconproject.customCalendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R

class adapter (
    private var items:List<List<dataModelDay>>,
    private var dates:List<String>,
    private val onClickListenerNext: (String) -> Unit ) : RecyclerView.Adapter<viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        return viewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_date,parent,false))
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val item = items[position]
        holder.render(item,dates,onClickListenerNext)
    }

    override fun getItemCount(): Int = items.size

}