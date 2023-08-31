package com.rojasdev.apprecconproject.customCalendar

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors.getColor
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.data.entities.SettingEntity
import com.rojasdev.apprecconproject.databinding.ItemDateBinding
import com.rojasdev.apprecconproject.databinding.ItemSettingsBinding
import java.time.LocalDate
import java.util.Calendar

class viewHolder( var view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemDateBinding.bind(view)

    @SuppressLint("ResourceAsColor")
    fun render(
        item: List<dataModelDay>,
        collection: List<String>,
        onClickListenerNext: (String) -> Unit
    ){
        dates(item,collection){
            onClickListenerNext(it)
        }
    }

    private fun dates (
        week: List<dataModelDay>,
        list: List<String>,
        onClickListener: (String) -> Unit){
        for(item in week){
            when (item.dayWeek) {
                "lunes" -> {
                    binding.monday.text = item.dayMonth
                    val result = item.dateTime in list
                    if (result){
                        binding.monday.setOnClickListener {
                            onClickListener(item.dateTime)
                            binding.monday.setBackgroundColor(Color.GRAY)
                        }
                        binding.ivMonday.visibility = View.VISIBLE
                    }else{
                        binding.monday.setTextColor(Color.LTGRAY)
                        binding.monday.setOnClickListener {}
                        binding.ivMonday.visibility = View.INVISIBLE
                    }
                }
                "martes" -> {
                    binding.tuesday.text = item.dayMonth
                    val result = item.dateTime in list
                    if (result){
                        binding.tuesday.setOnClickListener {
                            onClickListener(item.dateTime)
                            binding.tuesday.setBackgroundColor(Color.GRAY)
                        }
                        binding.ivTuesday.visibility = View.VISIBLE
                    }else{
                        binding.tuesday.setTextColor(Color.LTGRAY)
                        binding.tuesday.setOnClickListener {}
                        binding.ivTuesday.visibility = View.INVISIBLE
                    }
                }
                "miércoles" -> {
                    binding.wednesday.text = item.dayMonth
                    val result = item.dateTime in list
                    if (result){
                        binding.wednesday.setOnClickListener {
                            onClickListener(item.dateTime)
                            binding.wednesday.setBackgroundColor(Color.GRAY)
                        }
                        binding.ivWednesday.visibility = View.VISIBLE
                    }else{
                        binding.wednesday.setTextColor(Color.LTGRAY)
                        binding.wednesday.setOnClickListener {}
                        binding.ivWednesday.visibility = View.INVISIBLE
                    }
                }
                "jueves" -> {
                    binding.thursday.text = item.dayMonth
                    val result = item.dateTime in list
                    if (result){
                        binding.thursday.setOnClickListener {
                            onClickListener(item.dateTime)
                            binding.thursday.setBackgroundColor(Color.GRAY)
                        }
                        binding.ivThursday.visibility = View.VISIBLE
                    }else{
                        binding.thursday.setTextColor(Color.LTGRAY)
                        binding.thursday.setOnClickListener {}
                        binding.ivThursday.visibility = View.INVISIBLE
                    }
                }
                "viernes" -> {
                    binding.friday.text = item.dayMonth
                    val result = item.dateTime in list
                    if (result){
                        binding.friday.setOnClickListener {
                            onClickListener(item.dateTime)
                            binding.friday.setBackgroundColor(Color.GRAY)
                        }
                        binding.ivFriday.visibility = View.VISIBLE
                    }else{
                        binding.friday.setTextColor(Color.LTGRAY)
                        binding.friday.setOnClickListener {}
                        binding.ivFriday.visibility = View.INVISIBLE
                    }
                }
                "sábado" -> {
                    binding.saturday.text = item.dayMonth
                    val result = item.dateTime in list
                    if (result){
                        binding.saturday.setOnClickListener {
                            onClickListener(item.dateTime)
                            binding.saturday.setBackgroundColor(Color.GRAY)
                        }
                        binding.ivSaturday.visibility = View.VISIBLE
                    }else{
                        binding.saturday.setTextColor(Color.LTGRAY)
                        binding.saturday.setOnClickListener {}
                        binding.ivSaturday.visibility = View.INVISIBLE
                    }
                }
                "domingo" -> {
                    binding.sunday.text = item.dayMonth
                    binding.sunday.setTextColor(Color.RED)
                    val result = item.dateTime in list
                    if (result){
                        binding.sunday.setOnClickListener {
                            onClickListener(item.dateTime)
                            binding.sunday.setBackgroundColor(Color.GRAY)
                        }
                        binding.ivSunday.visibility = View.VISIBLE
                    }else{
                        binding.sunday.setOnClickListener {}
                        binding.ivSunday.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }
}