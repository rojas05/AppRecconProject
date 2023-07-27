package com.rojasdev.apprecconproject.alert

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.databinding.AlertDeleteBinding

class alertDeleteCollector(
    val nameCollector :String,
    val onClickListener: () -> Unit
): DialogFragment() {

    private lateinit var binding: AlertDeleteBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertDeleteBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        animatedAlert.animatedInit(binding.cvRecolector)

        binding.tvDetailDelete.text = nameCollector

        binding.brYes.setOnClickListener {
            onClickListener()
            dismiss()
        }

        binding.btNo.setOnClickListener {
            dismiss()
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }


}