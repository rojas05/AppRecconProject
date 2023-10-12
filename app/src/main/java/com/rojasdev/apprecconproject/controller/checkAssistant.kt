package com.rojasdev.apprecconproject.controller

import android.content.Context

object checkAssistant {
    fun start(context: Context, yes:()->Unit, now:()->Unit){
        val preferences = context.getSharedPreferences( "register", Context.MODE_PRIVATE)
        val assistantState = preferences.getString("assistant","")
        if(assistantState == "true"){
            yes()
        }else{
            now()
        }
    }
}