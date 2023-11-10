package com.rojasdev.apprecconprojectPro.viewHolders

import android.annotation.SuppressLint
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconprojectPro.R
import com.rojasdev.apprecconprojectPro.databinding.ItemCollecionBinding
import com.rojasdev.apprecconprojectPro.controller.price
import com.rojasdev.apprecconprojectPro.data.dataModel.collecionTotalCollector

class viewHolderCvCollectionTotal( var view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemCollecionBinding.bind(view)


    @SuppressLint("ResourceAsColor")
    fun render(
        item: collecionTotalCollector,
        onClickListener: (collecionTotalCollector) -> Unit
    ){
        binding.cv.animation = AnimationUtils.loadAnimation(view.context, R.anim.recycler_transition)
        binding.tvNameCollector.text = item.name_recolector
        binding.tvKg.text = "${item.kg_collection}kg"
        price.priceSplit(item.price_total.toInt()){
            binding.tvTotalPrice.text = it
        }

        binding.btReady.setOnClickListener {
            onClickListener(item)
        }
    }

}