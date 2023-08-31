package com.rojasdev.apprecconproject.controller

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText

object textListener {
    fun lister(textInputEditText: TextInputEditText,ready:()->Unit,finish:()->Unit){
        textInputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                finish()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                ready()
            }

            override fun afterTextChanged(s: Editable?) {
                if (textInputEditText.text!!.isEmpty()){
                    finish()
                }else{
                    ready()
                }
            }
        })
    }
}