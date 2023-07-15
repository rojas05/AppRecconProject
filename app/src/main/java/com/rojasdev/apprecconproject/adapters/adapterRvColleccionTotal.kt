package com.rojasdev.apprecconproject.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.data.dataModel.collecionTotalCollector
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.viewHolders.viewHolderCvCollectionTotal
import com.rojasdev.apprecconproject.viewHolders.viewHolderCvCollectors

class adapterRvColleccionTotal(
    private var items:List<collecionTotalCollector>,
    private val onClickListener: (collecionTotalCollector) -> Unit
) : RecyclerView.Adapter<viewHolderCvCollectionTotal>()

{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderCvCollectionTotal {
        return viewHolderCvCollectionTotal(LayoutInflater.from(parent.context).inflate(R.layout.item_collecion,parent,false,))
    }

    override fun onBindViewHolder(holder: viewHolderCvCollectionTotal, position: Int) {
        val item = items[position]
        holder.render(item,onClickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}