package com.rojasdev.apprecconproject.alert

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.textToSpeech
import com.rojasdev.apprecconproject.databinding.AlertAsistantBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class alertAssistant(
    var onClickListener: (String) -> Unit
): DialogFragment() {
    private lateinit var binding: AlertAsistantBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertAsistantBinding.inflate(LayoutInflater.from(context))
        animatedAlert.animatedInit(binding.cv)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        val message: String
        val preferences = requireContext().getSharedPreferences( "register", Context.MODE_PRIVATE)
        val assistantState = preferences.getString("assistant","")
        val dialog = builder.create()

        message = if(assistantState == "true"){
            binding.tvAssistant.setOnClickListener {
                onClickListener("false")
                dismiss()
            }
            "Desactivar el assistente"
        }else{
            binding.tvAssistant.setOnClickListener {
                onClickListener("true")
                initTts(dialog)
            }
            "Activar el assistente"
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        binding.tvAssistant.text = message

        CoroutineScope(Dispatchers.IO).launch {
            textToSpeech().start(
                requireContext(),
                message
            ){}
        }

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun initTts(dialog: AlertDialog) {
        binding.btnClose.setOnClickListener {}
        Toast.makeText(requireContext(), "iniciando el assitente", Toast.LENGTH_SHORT).show()
        dialog.setCanceledOnTouchOutside(false)
        animatedAlert.onBackAlert(dialog, requireContext(), "iniciando el assitente")
        CoroutineScope(Dispatchers.IO).launch {
            textToSpeech().start(
                requireContext(),
                "assistente activado"
            ) {
                dismiss()
            }
        }
    }
}
