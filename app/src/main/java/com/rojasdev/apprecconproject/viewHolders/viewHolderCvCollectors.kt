package com.rojasdev.apprecconproject.viewHolders

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.databinding.ItemCollectorBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class viewHolderCvCollectors( var view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemCollectorBinding.bind(view)


    @SuppressLint("ResourceAsColor")
    fun render(
        item: RecolectoresEntity,
        onClickListenerNext: (RecolectoresEntity) -> Unit,
        onClickListenerDelete: (RecolectoresEntity) -> Unit,
        onClickListenerKg: (RecolectoresEntity) -> Unit
    ){
        binding.cvCollector.animation = AnimationUtils.loadAnimation(view.context, R.anim.recycler_transition)
        binding.tvNameCollector.text = item.name

        CoroutineScope(Dispatchers.IO).launch {
            val collection = AppDataBase.getInstance(view.context).RecollectionDao().getCollectionIdCollector(item.id!!)
            launch(Dispatchers.Main) {
                if (collection.isNotEmpty()){
                    binding.fbDeleteCollector.alpha = 0f
                    binding.fbDeleteCollector.isClickable = false
                }
            }
        }

        binding.fbDeleteCollector.setOnClickListener {
            onClickListenerDelete(item)
        }
        binding.cvCollector.setOnClickListener {
            onClickListenerNext(item)
        }
        binding.ivNext.setOnClickListener{
            onClickListenerNext(item)
        }
        binding.fbAddKg.setOnClickListener {
            onClickListenerKg(item)
        }

    }

}