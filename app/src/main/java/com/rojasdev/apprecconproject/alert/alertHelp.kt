package com.rojasdev.apprecconproject.alert

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.textToSpeech
import com.rojasdev.apprecconproject.databinding.AlertHelpBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class alertHelp(
    var onClickListener: () -> Unit
): DialogFragment() {
    private lateinit var binding: AlertHelpBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertHelpBinding.inflate(LayoutInflater.from(context))
        animatedAlert.animatedInit(binding.cv)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        binding.lyChat.setOnClickListener {
            onClickListener()
            dismiss()
        }
        binding.btnClose.setOnClickListener {
            dismiss()
        }

        CoroutineScope(Dispatchers.IO).launch {
            textToSpeech().start(
                requireContext(),
                binding.textView2.text.toString()
            ) {}
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        animatedAlert.onBackAlert(dialog,requireContext(),getString(R.string.requireDates))
        return dialog
    }


}