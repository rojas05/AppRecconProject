package com.rojasdev.apprecconproject.alert

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.textToSpeech
import com.rojasdev.apprecconproject.databinding.AlertWelcomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.marc_apps.tts.TextToSpeechInstance
import nl.marc_apps.tts.errors.TextToSpeechSynthesisInterruptedError

class alertWelcome(
    var onClickListener: () -> Unit
): DialogFragment() {

    private lateinit var binding: AlertWelcomeBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertWelcomeBinding.inflate(LayoutInflater.from(context))
        animatedAlert.animatedInit(binding.cvWelcome)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        buttons(null)

        CoroutineScope(Dispatchers.IO).launch {
            textToSpeech().start(
                requireContext(),
                getString(R.string.assistantWelcome)
            ){ buttons(it) }
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        animatedAlert.onBackAlert(dialog,requireContext(),getString(R.string.go))
        return dialog
    }

    private fun buttons (tts: TextToSpeechInstance?){
        if (tts == null){
            binding.btInit.setOnClickListener{
                dismiss()
                onClickListener()
            }
        } else {
            binding.btInit.setOnClickListener{
                dismiss()
                onClickListener()
                try {
                    tts.close()
                } catch (e: TextToSpeechSynthesisInterruptedError) {
                    Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}