package com.rojasdev.apprecconproject.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.data.dataModel.collectorCollection
import com.rojasdev.apprecconproject.viewHolders.viewHolderCvRecollection

class adapterRvRecolection(
                private var item: List<collectorCollection>,
                private var onClickListenerUpdate: (collectorCollection) -> Unit
        ):RecyclerView.Adapter<viewHolderCvRecollection>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderCvRecollection {
                return viewHolderCvRecollection(LayoutInflater.from(parent.context).inflate(R.layout.item_rv_recolection, parent, false))
        }

        override fun onBindViewHolder(holder: viewHolderCvRecollection, position: Int) {
                val item = item[position]
                holder.render(item,onClickListenerUpdate)
        }

        override fun getItemCount(): Int = item.size

}