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
import com.rojasdev.apprecconproject.controller.requireInput
import com.rojasdev.apprecconproject.data.entities.SettingEntity
import com.rojasdev.apprecconproject.databinding.AlertUpdateSettingBinding

class alertSettingsUpdate(
    var description: String,
    var feending: String,
    var idSetting: Int,
    var price: Int,
    var onClickListener: (SettingEntity) -> Unit
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

        binding.fbClose.setOnClickListener {
            dismiss()
        }

        initView()

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun initView() {
        binding.yesAliment.setText(price.toString())
        binding.tvDescription.text = description
        if (feending == "yes"){
            binding.tilSiAlimentacion.setStartIconDrawable(R.drawable.ic_alimentacion)
            binding.yesAliment.setHint(R.string.si_alimentacion)
        }else{
            binding.tilSiAlimentacion.setStartIconDrawable(R.drawable.ic_no_alimentacion)
            binding.yesAliment.setHint(R.string.no_alimentacion)
        }
    }

    private fun dates() {
        val yesAliment = binding.yesAliment.text.toString()

        val configAlimentYes = SettingEntity(
            idSetting,
            feending,
            yesAliment.toInt(),
            "active"
        )

        onClickListener(configAlimentYes)
    }
}