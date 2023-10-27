package com.rojasdev.apprecconproject.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.data.dataModel.collectorCollection
import com.rojasdev.apprecconproject.viewHolders.viewHolderCancelCollection

class adapterRvCancelCollection(
        private var items:List<collectorCollection>
    ) : RecyclerView.Adapter<viewHolderCancelCollection>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderCancelCollection {
        return viewHolderCancelCollection(LayoutInflater.from(parent.context).inflate(R.layout.item_collection_cancel,parent,false))
    }

    override fun onBindViewHolder(holder: viewHolderCancelCollection, position: Int) {
        val item = items[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = items.size
}