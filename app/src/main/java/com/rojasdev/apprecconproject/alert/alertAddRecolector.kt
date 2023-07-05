package com.rojasdev.apprecconproject.alert

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputLayout
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.requireInput
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.databinding.AlertRecolectonBinding

class alertAddRecolector(
    val onClickListener: (RecolectoresEntity) -> Unit
): DialogFragment() {

    private lateinit var binding: AlertRecolectonBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertRecolectonBinding.inflate(LayoutInflater.from(context))

        animatedAlert.animatedInit(binding.cvRecolector)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        val myListInput = listOf(
            binding.yesAddRecolector
        )

        binding.btAddRecolector.setOnClickListener {
           val required = requireInput.validate(myListInput,requireContext())
                if (required){
                    dates()
                    binding.yesAddRecolector.setText("")
                }
        }

        binding.btnClose.setOnClickListener{
            dismiss()
        }

        val dialog = builder.create()
              dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    return dialog
    }

    private fun dates() {
        val recolector = binding.yesAddRecolector.text.toString()
        val addUser = RecolectoresEntity(
            null,
            recolector,
            null
        )
            onClickListener(addUser)
    }

}
