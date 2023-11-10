package com.rojasdev.apprecconprojectPro.alert

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconprojectPro.R
import com.rojasdev.apprecconprojectPro.databinding.AlertWelcomeBinding
import com.rojasdev.apprecconprojectPro.controller.animatedAlert

class alertWelcome(
    var onClickListener: () -> Unit
): DialogFragment() {
    private lateinit var binding: AlertWelcomeBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = AlertWelcomeBinding.inflate(LayoutInflater.from(context))

        animatedAlert.animatedInit(binding.cvWelcome)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        binding.btInit.setOnClickListener {
            onClickListener()
            dismiss()
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        animatedAlert.onBackAlert(dialog,requireContext(),getString(R.string.go))
        return dialog
    }
}