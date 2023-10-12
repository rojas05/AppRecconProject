package com.rojasdev.apprecconproject.controller

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.marc_apps.tts.TextToSpeechInstance
import nl.marc_apps.tts.errors.TextToSpeechInitialisationError
import nl.marc_apps.tts.errors.TextToSpeechSynthesisInterruptedError
import nl.marc_apps.tts.errors.UnknownTextToSpeechSynthesisError

class textToSpeech{
    var tts: TextToSpeechInstance? = null

    @Throws(TextToSpeechInitialisationError::class)
    suspend fun start(
        context: Context,
        message: String,
        onStop: (TextToSpeechInstance)->Unit) {
        checkAssistant.start(
            context,
            { CoroutineScope(Dispatchers.IO).launch {init(context,message,onStop)}},
            {}
        )
    }

    suspend fun init(
        context: Context,
        message: String,
        onStop: (TextToSpeechInstance)->Unit) {
        try {
            // Use TextToSpeech.createOrNull to ignore errors.
            tts = tts ?: nl.marc_apps.tts.TextToSpeech.createOrThrow(context)
            tts!!.rate = 1f
            // Use status STARTED to resume coroutine when the TTS engine starts speaking. The status of FINISHED will wait until the TTS engine has finished speaking.
            tts?.say(message, clearQueue = false, resumeOnStatus = TextToSpeechInstance.Status.STARTED)
            onStop(tts!!)
        } catch (e: UnknownTextToSpeechSynthesisError) {
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(context, "problema de inicializacion del assistente", Toast.LENGTH_SHORT).show()
                launch {
                    textToSpeech().init(
                        context,
                        message
                    ) { onStop(tts!!) }
                }
            }
        }
    }

}