package com.rojasdev.apprecconprojectPro.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconprojectPro.R
import com.rojasdev.apprecconprojectPro.data.entities.RecolectoresEntity
import com.rojasdev.apprecconprojectPro.viewHolders.viewHolderCvCollectors

class adapterRvCollectors(
    private var items:List<RecolectoresEntity>,
    private var list: List<Long>,
    private val onClickListenerNext: (RecolectoresEntity) -> Unit,
    private val onClickListenerDelete: (RecolectoresEntity) -> Unit,
    private val onClickListenerKg: (RecolectoresEntity) -> Unit) : RecyclerView.Adapter<viewHolderCvCollectors>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderCvCollectors {
        return viewHolderCvCollectors(LayoutInflater.from(parent.context).inflate(R.layout.item_collector,parent,false,))
    }

    override fun onBindViewHolder(holder: viewHolderCvCollectors, position: Int) {
        val item = items[position]
        holder.render(item,list,onClickListenerNext,onClickListenerDelete,onClickListenerKg)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}