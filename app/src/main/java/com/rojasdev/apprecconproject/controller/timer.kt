package com.rojasdev.apprecconproject.controller

import android.os.CountDownTimer

object timer {
    fun starTimer(ready:()->Unit) {
        object: CountDownTimer(3000,1){
            override fun onTick(p0: Long) {
            }
            override fun onFinish() {
                ready()
            }
        }.start()
    }
}