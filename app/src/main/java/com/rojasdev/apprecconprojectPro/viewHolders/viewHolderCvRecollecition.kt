package com.rojasdev.apprecconprojectPro.viewHolders

import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconprojectPro.R
import com.rojasdev.apprecconprojectPro.controller.price
import com.rojasdev.apprecconprojectPro.data.dataModel.collectorCollection
import com.rojasdev.apprecconprojectPro.databinding.ItemRvRecolectionBinding

class viewHolderCvRecollecition( var view: View ): RecyclerView.ViewHolder(view) {

    private val binding = ItemRvRecolectionBinding.bind(view)

    fun render(
        itemDetail: collectorCollection,
        onClickListenerUpdate: (collectorCollection) -> Unit
    ) {

        binding.cvCollectionDetail.animation = AnimationUtils.loadAnimation(view.context, R.anim.recycler_transition)

            binding.tvNameCollector.text = "Fecha: ${itemDetail.Fecha}"
            binding.tvKgDetail.text = "${itemDetail.Cantidad} Kg"

        binding.btnUpdate.setOnClickListener {
            onClickListenerUpdate(itemDetail)
        }

        price.priceSplit(itemDetail.Precio.toInt()){
            binding.tvDate.text = "Precio: ${it}"
        }

        if (itemDetail.Alimentacion == "yes") {
            binding.tvFeending.text = "Alimentacion: Si"
        } else {
            binding.tvFeending.text = "Alimentacion: No"
        }

    }
}