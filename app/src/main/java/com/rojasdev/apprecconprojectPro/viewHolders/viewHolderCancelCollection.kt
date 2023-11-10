package com.rojasdev.apprecconprojectPro.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconprojectPro.controller.price
import com.rojasdev.apprecconprojectPro.data.dataModel.collectorCollection
import com.rojasdev.apprecconprojectPro.databinding.ItemCollectionCancelBinding


class viewHolderCancelCollection( var view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemCollectionCancelBinding.bind(view)

    fun render(
        item: collectorCollection
    ){
        if (item.Alimentacion.equals("yes")){
            binding.tvAliment.text = "si ${item.Precio.toInt()}"
        }else{
            binding.tvAliment.text = "no ${item.Precio.toInt()}"
        }

        binding.tvKg.text = "${item.Cantidad}kg"
        price.priceSplit(item.result.toInt()){
            binding.tvPrice.text = it
        }
    }

}