package com.rojasdev.apprecconproject.controller

import android.view.KeyEvent
import android.view.View
import com.google.android.material.textfield.TextInputEditText

object keyLIstener {

    fun start (input: TextInputEditText, onClickKey:()->Unit){
        input.setOnKeyListener(object : View.OnKeyListener {

            //onKey() funcion que permite identificar cada event y keyCode
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {

                //cuando la tecla se suelte realizara la busqueda,
                //esto es pocible con el ACTION_UP
                if (event.action == KeyEvent.ACTION_UP &&

                    //para optener la tecla ENTER en especifio se realiza de la siguiente manera,
                    //dentro del condicional
                    keyCode == KeyEvent.KEYCODE_ENTER) {
                        onClickKey()
                    return true
                }
                return false
            }
        })
    }

}