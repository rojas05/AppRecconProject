package com.rojasdev.apprecconproject.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.data.entities.SettingEntity
import com.rojasdev.apprecconproject.viewHolders.viewHolderSettings

class adapterRvSettings (
    private var items:List<SettingEntity>,
    private val onClickListenerNext: (SettingEntity) -> Unit ) : RecyclerView.Adapter<viewHolderSettings>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolderSettings {
        return viewHolderSettings(LayoutInflater.from(parent.context).inflate(R.layout.item_settings, parent,false))
    }

    override fun onBindViewHolder(holder: viewHolderSettings, position: Int) {
        val item = items[position]
        holder.render(item,onClickListenerNext)
    }

    override fun getItemCount(): Int = items.size
}