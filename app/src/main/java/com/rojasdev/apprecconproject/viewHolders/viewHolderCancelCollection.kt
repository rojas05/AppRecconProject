package com.rojasdev.apprecconproject.viewHolders

import android.annotation.SuppressLint
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.data.dataModel.collecionTotalCollector
import com.rojasdev.apprecconproject.data.dataModel.collectorCollection
import com.rojasdev.apprecconproject.databinding.ItemCollecionBinding
import com.rojasdev.apprecconproject.databinding.ItemCollectionCancelBinding

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