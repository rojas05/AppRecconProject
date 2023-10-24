package com.rojasdev.apprecconproject.alert

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconproject.ActivityRecolectionDetail
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.keyLIstener
import com.rojasdev.apprecconproject.controller.requireInput
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.databinding.AlertUpdateSettingBinding

class alertUpdateNameCollector (
    var name: String,
    var idCollector: Int,
    var onClickListener: (RecolectoresEntity) -> Unit
): DialogFragment() {
    private lateinit var binding: AlertUpdateSettingBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertUpdateSettingBinding.inflate(LayoutInflater.from(context))

        animatedAlert.animatedInit(binding.cvSettings)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        val myListInput = listOf(
            binding.yesAliment
        )

        binding.btReady.setOnClickListener {
            val require = requireInput.validate(myListInput,requireContext())
            if (require){
                dates()
                dismiss()
            }
        }

        keyLIstener.start(binding.yesAliment){
            val require = requireInput.validate(myListInput,requireContext())
            if (require){
                dates()
                dismiss()
            }
        }

        binding.fbClose.setOnClickListener {
            dismiss()
        }

        initView()

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun initView() {
        binding.yesAliment.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        binding.yesAliment.setText(name)
        binding.tvDescription.text = getString(R.string.updateName)
        binding.tilSiAlimentacion.setStartIconDrawable(R.drawable.ic_recolector)

    }

    private fun dates() {
        val newName = binding.yesAliment.text.toString()

        val editNameCollector = RecolectoresEntity(
            idCollector,
            newName,
            "active"
        )
        onClickListener(editNameCollector)

        startActivity(Intent(
            requireContext(), ActivityRecolectionDetail::class.java)
            .putExtra("userId", idCollector).putExtra("userName", newName) // Pasar parametros
        )
    }
}