package com.rojasdev.apprecconproject.alert

import android.annotation.SuppressLint
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
import com.rojasdev.apprecconproject.databinding.AlertInfoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.marc_apps.tts.TextToSpeechInstance
import nl.marc_apps.tts.errors.TextToSpeechSynthesisInterruptedError

class alertMessage(
    private val messageA: String,
    private val messageB: String,
    private val btnYes: String,
    private val btnNo: String,
    val message: String,
    var onClickListener: (String) -> Unit ): DialogFragment() {

    private lateinit var binding: AlertInfoBinding
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertInfoBinding.inflate(LayoutInflater.from(context))
        animatedAlert.animatedInit(binding.cvWelcome)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        val messageAssistant = "$message \n ${messageA.replace("$", "")} " +
                               "\n ${messageB.replace("$", "")}"

        CoroutineScope(Dispatchers.IO).launch {
            textToSpeech().start(
                requireContext(),
                messageAssistant
            ){
                buttons(it)
            }
        }

        binding.tvMessage.text = message
        binding.btYes.text = btnYes
        binding.btNo.text = btnNo
        binding.tvMessageA.text = messageA
        binding.tvMessageB.text = messageB

        buttons(null)

        val dialog = builder.create()
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCanceledOnTouchOutside(false)
        animatedAlert.onBackAlert(dialog,requireContext(),getString(R.string.requireDates))
        return dialog
    }

    private fun buttons (tts: TextToSpeechInstance?){
        if (tts == null){
            binding.btYes.setOnClickListener {
                onClickListener("yes")
                dismiss()
            }
            binding.btNo.setOnClickListener {
                onClickListener("no")
                dismiss()
            }
        } else {
            binding.btYes.setOnClickListener {
                onClickListener("yes")
                dismiss()

                try {
                    tts.close()
                } catch (e: ArithmeticException) {
                    Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            binding.btNo.setOnClickListener {
                onClickListener("no")
                dismiss()

                try {
                    tts.close()
                } catch (e: TextToSpeechSynthesisInterruptedError) {
                    Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}