package com.rojasdev.apprecconproject.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.data.dataModel.collectorCollection
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.data.entities.RecollectionEntity
import com.rojasdev.apprecconproject.viewHolders.viewHolderCvRecollecition

class adpaterRvRecolection(
                private var item: List<collectorCollection>,
                private var onClickListenerUpdate: (collectorCollection) -> Unit
        ):RecyclerView.Adapter<viewHolderCvRecollecition>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderCvRecollecition {
                return viewHolderCvRecollecition(LayoutInflater.from(parent.context).inflate(R.layout.item_rv_recolection, parent, false))
        }

        override fun onBindViewHolder(holder: viewHolderCvRecollecition, position: Int) {
                val item = item[position]
                holder.render(item,onClickListenerUpdate)
        }

        override fun getItemCount(): Int = item.size

}