package com.rojasdev.apprecconproject.controller

import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.Context
import android.view.KeyEvent
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.cardview.widget.CardView

object animatedAlert {
    fun animatedInit(cv: CardView){
        cv.alpha = 0f
        val animator = ValueAnimator.ofFloat(0f,1f)
        animator.duration = 800
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener { valueAnimator ->
            val animatedValues = valueAnimator.animatedValue as Float
            cv.alpha = animatedValues
            cv.scaleX = animatedValues
            cv.scaleY = animatedValues
        }
        animator.start()
    }

    fun animatedClick(cv: CardView){
        cv.alpha = 1f
        val animator = ValueAnimator.ofFloat(1f,0.8f,1f)
        animator.duration = 400
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener { valueAnimator ->
            val animatedValues = valueAnimator.animatedValue as Float
            cv.alpha = animatedValues
            cv.scaleX = animatedValues
            cv.scaleY = animatedValues
        }
        animator.start()
    }

    fun onBackAlert(dialog: AlertDialog, contex : Context, mensaje : String){
        dialog.setOnKeyListener { _, keyCode,_ ->
            if (keyCode == KeyEvent.KEYCODE_BACK){
                Toast.makeText(contex,mensaje, Toast.LENGTH_LONG).show()
                true
            }else{
                false
            }
        }
    }

}