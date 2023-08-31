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
import com.rojasdev.apprecconproject.databinding.AlertInfoBinding

class alertMessage(
    val message: String,
    val messageA: String,
    val messageB: String,
    val btnYes: String,
    val btnNo: String,
    var onClickListener: (String) -> Unit
): DialogFragment() {
    private lateinit var binding: AlertInfoBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertInfoBinding.inflate(LayoutInflater.from(context))
        animatedAlert.animatedInit(binding.cvWelcome)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        binding.tvMessage.text = message
        binding.btYes.text = btnYes
        binding.btNo.text = btnNo
        binding.tvMessageA.text = messageA
        binding.tvMessageB.text = messageB

        binding.btYes.setOnClickListener {
            onClickListener("yes")
            dismiss()
        }
        binding.btNo.setOnClickListener {
            onClickListener("no")
            dismiss()
        }


        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        animatedAlert.onBackAlert(dialog,requireContext(),getString(R.string.requireDates))
        return dialog
    }


}