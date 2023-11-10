package com.rojasdev.apprecconprojectPro.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconprojectPro.R
import com.rojasdev.apprecconprojectPro.data.dataModel.collecionTotalCollector
import com.rojasdev.apprecconprojectPro.viewHolders.viewHolderCvCollectionTotal

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