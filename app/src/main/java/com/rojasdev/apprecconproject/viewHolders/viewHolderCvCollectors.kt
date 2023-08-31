package com.rojasdev.apprecconproject.viewHolders

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.ActivityRecolectionDetail
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.databinding.ItemCollectorBinding
import com.rojasdev.apprecconproject.databinding.ItemRvRecolectionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class viewHolderCvCollectors( var view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemCollectorBinding.bind(view)

    @SuppressLint("ResourceAsColor")
    fun render(
        item: RecolectoresEntity,
        list: List<Long>,
        onClickListenerNext: (RecolectoresEntity) -> Unit,
        onClickListenerDelete: (RecolectoresEntity) -> Unit,
        onClickListenerKg: (RecolectoresEntity) -> Unit
    ){
        binding.cvCollector.animation = AnimationUtils.loadAnimation(view.context, R.anim.recycler_transition)
        binding.tvNameCollector.text = item.name

        val result = item.id!!.toLong() in list

        if (result){
            binding.fbDeleteCollector.setImageResource(R.drawable.ic_bolsa_de_cafe)
            binding.tvDeleteAndDetail.text = "Detalle"
            binding.fbDeleteCollector.setOnClickListener {
                onClickListenerNext(item)
                animationOnCLick()
            }
            binding.viewHeaderBackground.setOnClickListener {
                onClickListenerNext(item)
                animationOnCLick()
            }
        }else{
            binding.tvDeleteAndDetail.text = "Eliminar"
            binding.fbDeleteCollector.setOnClickListener {
                onClickListenerDelete(item)
                animationOnCLick()
            }
            binding.viewHeaderBackground.setOnClickListener {
                onClickListenerDelete(item)
                animationOnCLick()
            }
        }

        binding.fbAddKg.setOnClickListener {
            onClickListenerKg(item)
            animationOnCLick()
        }

    }

     // Animation
    private fun animationOnCLick() { animatedAlert.animatedClick(binding.cvCollector) }

}