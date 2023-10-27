package com.rojasdev.apprecconproject.viewHolders

import android.annotation.SuppressLint
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.data.entities.SettingEntity
import com.rojasdev.apprecconproject.databinding.ItemSettingsBinding

class viewHolderSettings( var view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemSettingsBinding.bind(view)

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    fun render(
        item: SettingEntity,
        onClickListenerNext: (SettingEntity) -> Unit,
    ){
        binding.lyItem.animation = AnimationUtils.loadAnimation(view.context, R.anim.recycler_transition)

        binding.tvDate.text = item.date.substring(0,item.date.length - 9)

        if(item.feeding == "yes"){
            binding.tvAliment.text = "Precio por kilogramo con alimentacion"
            price.priceSplit(item.cost){
                binding.tvAlimentPrice.text = it
            }
        }else{
            binding.tvAliment.text = "Precio por kilogramo sin alimentacion"
            price.priceSplit(item.cost){
                binding.tvAlimentPrice.text = it
            }
        }
        binding.lyItem.setOnClickListener {
            onClickListenerNext(item)
        }
    }

}