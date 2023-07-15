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


    @SuppressLint("ResourceAsColor")
    fun render(
        item: collectorCollection
    ){
        binding.tvAliment.text = item.Alimentacion
        binding.tvKg.text = "${item.Cantidad}kg"
        price.priceSplit(item.Precio.toInt()){
            binding.tvPrice.text = it
        }
    }

}