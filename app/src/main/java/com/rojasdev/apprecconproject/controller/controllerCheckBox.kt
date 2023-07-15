package com.rojasdev.apprecconproject.controller

import android.content.Context
import android.widget.CheckBox
import android.widget.TextView
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object controllerCheckBox {
    fun checkBoxFun(cbNo:CheckBox, cbYes:CheckBox, tvAliment:TextView, context: Context, ready:(Int) -> Unit) {
        if(cbNo.isChecked){
            CoroutineScope(Dispatchers.IO).launch{
                val query = AppDataBase.getInstance(context).SettingDao().getAliment("no")
                launch(Dispatchers.Main) {
                    ready(query[0].Id!!)
                }
            }
        }else if(cbYes.isChecked){
            CoroutineScope(Dispatchers.IO).launch{
                val query = AppDataBase.getInstance(context).SettingDao().getAliment("yes")
                launch(Dispatchers.Main) {
                    ready(query[0].Id!!)
                }
            }
        }else{
            tvAliment.error = "indique la alimentacion"
            tvAliment.requestFocus()
            requireInput.vibratePhone(context)
        }
    }
}