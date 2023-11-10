package com.rojasdev.apprecconprojectPro.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconprojectPro.R
import com.rojasdev.apprecconprojectPro.data.dataModel.collectorCollection
import com.rojasdev.apprecconprojectPro.viewHolders.viewHolderCancelCollection

class adapterRvcancelCollection(
    private var items:List<collectorCollection>
) : RecyclerView.Adapter<viewHolderCancelCollection>()

{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderCancelCollection {
        return viewHolderCancelCollection(LayoutInflater.from(parent.context).inflate(R.layout.item_collection_cancel,parent,false,))
    }

    override fun onBindViewHolder(holder: viewHolderCancelCollection, position: Int) {
        val item = items[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}