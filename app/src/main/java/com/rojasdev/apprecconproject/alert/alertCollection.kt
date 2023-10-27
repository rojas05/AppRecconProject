package com.rojasdev.apprecconproject.alert

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.controllerCheckBox
import com.rojasdev.apprecconproject.controller.dateFormat
import com.rojasdev.apprecconproject.controller.keyLIstener
import com.rojasdev.apprecconproject.controller.requireInput
import com.rojasdev.apprecconproject.controller.textToSpeech
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.data.entities.RecollectionEntity
import com.rojasdev.apprecconproject.databinding.AlertCollectionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.marc_apps.tts.TextToSpeechInstance

class alertCollection (
    private var collector: RecolectoresEntity,
    var onClickListener: (RecollectionEntity) -> Unit
): DialogFragment() {
    private var settingsId: Int? = null
    private lateinit var binding: AlertCollectionBinding
    private lateinit var tts: TextToSpeech

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertCollectionBinding.inflate(LayoutInflater.from(context))
        tts = TextToSpeech(context){}
        animatedAlert.animatedInit(binding.cvRecolector)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        binding.tvDescription.text = collector.name

        binding.cbYes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cbNo.isChecked = false
            }
        }

        binding.cbNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.cbYes.isChecked = false
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            textToSpeech().start(
                requireContext(),
                getString(R.string.assistantAddCollection).replace("name", collector.name)
            ){
                buttons(it)
            }
        }

        buttons(null)

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun buttons(tts: TextToSpeechInstance?) {
        if (tts == null){
            binding.btnClose.setOnClickListener {
                dismiss()
            }

            val myListInput = listOf(
                binding.etKg
            )

            keyLIstener.start(binding.etKg){
                val require = requireInput.validate(myListInput,requireContext())
                if (require){
                    controllerCheckBox.checkBoxFun(
                        binding.cbNo,
                        binding.cbYes,
                        binding.tvAliment,
                        requireContext()
                    ){
                        settingsId = it
                        dates()
                        dismiss()
                    }
                }
            }

            binding.btReady.setOnClickListener {
                val require = requireInput.validate(myListInput,requireContext())
                if (require){
                    controllerCheckBox.checkBoxFun(
                        binding.cbNo,
                        binding.cbYes,
                        binding.tvAliment,
                        requireContext()
                    ){
                        settingsId = it
                        dates()
                        dismiss()
                    }
                }
            }
        } else{
            binding.btnClose.setOnClickListener {
                dismiss()
                tts.close()
            }

            binding.btReady.setOnClickListener {
                val myListInput = listOf(
                    binding.etKg
                )

                val require = requireInput.validate(myListInput,requireContext())
                if (require){
                    controllerCheckBox.checkBoxFun(
                        binding.cbNo,
                        binding.cbYes,
                        binding.tvAliment,
                        requireContext()
                    ){
                        settingsId = it
                        dates()
                        tts.close()
                        dismiss()
                    }
                }
            }
        }
    }

    private fun dates() {
        val kg = binding.etKg.text.toString()

        val collection = RecollectionEntity(
            null,
            kg.toDouble(),
            dateFormat.main(),
            "active",
            collector.id!!,
            settingsId!!
        )

        onClickListener(collection)
    }
}