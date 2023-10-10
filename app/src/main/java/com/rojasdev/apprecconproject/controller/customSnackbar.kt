package com.rojasdev.apprecconproject.controller

import android.graphics.Color
import android.media.MediaPlayer
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.databinding.SnackbarBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object customSnackbar {
    fun showCustomSnackbar(view: android.view.View, message: String) {
        val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT)

        // Personaliza la vista de la Snackbar
        val snackbarView = snackbar.view
        val snackbarLayout = snackbarView as Snackbar.SnackbarLayout
        snackbarLayout.setBackgroundColor(ContextCompat.getColor(view.context, R.color.transparent))

        // Infla el diseño de la Snackbar personalizada utilizando ViewBinding
        val binding = SnackbarBinding.inflate(LayoutInflater.from(view.context))
        binding.tv.text = message
        binding.tv.setTextColor(Color.BLACK)

        // Agrega la vista personalizada a la Snackbar
        snackbarLayout.addView(binding.root)

        val mp = MediaPlayer.create(view.context,R.raw.notification_all_tasks_completed)
        mp.start()

        CoroutineScope(Dispatchers.IO).launch {
            textToSpeech().start(view.context, message){}
        }
        snackbar.show()
    }
}