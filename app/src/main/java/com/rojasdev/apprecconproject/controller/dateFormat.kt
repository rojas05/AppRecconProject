package com.rojasdev.apprecconproject.controller

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object dateFormat {
    fun main():String{
        val date = Calendar.getInstance().time
        val formatDate = "yyyy-MM-dd HH:mm:ss"
        val formato = SimpleDateFormat(formatDate, Locale("es", "CO"))
        val dateFormat = formato.format(date)
        return dateFormat.toString()
    }

    fun format(date:String):Pair<String,String>{
        val getDate = date
        val formatDateOriginal = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("es", "CO"))
        val formatDate = SimpleDateFormat("EEEE,dd 'de 'MMMM 'del' yyyy", Locale("es", "CO"))
        val formatHour = SimpleDateFormat("'Hora: ' HH:mm", Locale("es", "CO"))
        val date = formatDateOriginal.parse(getDate)
        val timeFormat = formatHour.format(date!!) // Hora
        val dateFormat = formatDate.format(date) // Fecha
        return Pair(dateFormat,timeFormat)
    }

}