package com.rojasdev.apprecconprojectPro.controller

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import com.google.android.material.textfield.TextInputEditText

object requireInput {
    fun validate(inputList: List<TextInputEditText>,context: Context ):Boolean{
        for (item in inputList){
            if (item.text!!.isEmpty()){
                item.error = "¡Campo obligatorio!"
                item.requestFocus()
                vibratePhone(context)
                return false
            }
        }
        return true
    }

   fun vibratePhone(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }
}