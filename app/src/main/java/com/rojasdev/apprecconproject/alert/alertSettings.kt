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
import com.rojasdev.apprecconproject.controller.dateFormat
import com.rojasdev.apprecconproject.controller.requireInput
import com.rojasdev.apprecconproject.controller.textToSpeech
import com.rojasdev.apprecconproject.data.entities.SettingEntity
import com.rojasdev.apprecconproject.databinding.AlertSettinsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.marc_apps.tts.TextToSpeechInstance
import nl.marc_apps.tts.errors.TextToSpeechSynthesisInterruptedError

class alertSettings(
    var onClickListener: (SettingEntity) -> Unit
): DialogFragment() {
    private lateinit var binding: AlertSettinsBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertSettinsBinding.inflate(LayoutInflater.from(context))
        animatedAlert.animatedInit(binding.cvSettings)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        CoroutineScope(Dispatchers.IO).launch {
            textToSpeech().start(
                requireContext(),
                getString(R.string.assistantRequire)
            ){ buttons(it) }
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        animatedAlert.onBackAlert(dialog,requireContext(),getString(R.string.requireDates))
        return dialog
    }

    private fun buttons (tts: TextToSpeechInstance?){
        val myListInput = listOf(
            binding.yesAliment,
            binding.nowAliment
        )

        if (tts == null){
            binding.btReady.setOnClickListener {
                val require = requireInput.validate(myListInput,requireContext())
                if (require){
                    dates()
                    dismiss()
                }
            }
        } else {
            binding.btReady.setOnClickListener {
                val require = requireInput.validate(myListInput,requireContext())
                if (require){
                    dates()
                    dismiss()
                }

                try {
                    tts.close()
                } catch (e: TextToSpeechSynthesisInterruptedError) {
                    Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun dates() {
        val yesAliment = binding.yesAliment.text.toString()
        val nowAliment = binding.nowAliment.text.toString()

        val configAlimentYes = SettingEntity(
            null,
            "yes",
            yesAliment.toInt(),
            "active",
            dateFormat.main()
        )
        val configAlimentNow = SettingEntity(
            null,
            "no",
            nowAliment.toInt(),
            "active",
            dateFormat.main()
        )

        onClickListener(configAlimentNow)
        onClickListener(configAlimentYes)
    }
}


