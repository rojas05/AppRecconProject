package com.rojasdev.apprecconproject.alert

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.adapters.adapterRvCancelCollection
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.controller.textToSpeech
import com.rojasdev.apprecconproject.data.dataModel.collecionTotalCollector
import com.rojasdev.apprecconproject.data.dataModel.collectorCollection
import com.rojasdev.apprecconproject.databinding.AlertCancelCollectionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.marc_apps.tts.TextToSpeechInstance
import nl.marc_apps.tts.errors.TextToSpeechSynthesisInterruptedError

class alertCancelCollection (
    private var collectionTotal: List<collecionTotalCollector>,
    val collection: List<collectorCollection>,
    var onClickListener: (Int) -> Unit
): DialogFragment() {
    private lateinit var adapter: adapterRvCancelCollection
    private lateinit var binding: AlertCancelCollectionBinding
    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertCancelCollectionBinding.inflate(LayoutInflater.from(context))
        animatedAlert.animatedInit(binding.cvRecolector)
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        CoroutineScope(Dispatchers.IO).launch {
            textToSpeech().start(
                requireContext(),
                "${getString(R.string.assistantCancelCollection)} \n" +
                        "${collectionTotal[0].name_recolector} \n" +
                        "${collectionTotal[0].kg_collection}Kilogramos \n" +
                        "${getString(R.string.assistantCancelCollectionTotal)} \n" +
                        "${collectionTotal[0].price_total.toInt()} COP"
            ){
                buttons(it)
            }
        }

        buttons(null)

        binding.tvNameCollector.text = collectionTotal[0].name_recolector


        binding.tvKg.text = "${collectionTotal[0].kg_collection} Kg"
        price.priceSplit(collectionTotal[0].price_total.toInt()){
            binding.tvTotalPrice.text = it
        }

        dates()

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }
    private fun dates() {
        adapter = adapterRvCancelCollection(collection)
        binding.rv.adapter = adapter
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun buttons (tts: TextToSpeechInstance?){
        if (tts == null){
            binding.btnReady.setOnClickListener {
                onClickListener(collectionTotal[0].PK_ID_Recolector)
                dismiss()
            }

            binding.btnClose.setOnClickListener {
                dismiss()
            }

            binding.btnFinish.setOnClickListener {
                dismiss()
            }
        } else {
            binding.btnReady.setOnClickListener {
                onClickListener(collectionTotal[0].PK_ID_Recolector)
                dismiss()

                try {
                    tts.close()
                } catch (e: TextToSpeechSynthesisInterruptedError) {
                    Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            binding.btnClose.setOnClickListener {
                dismiss()

                try {
                    tts.close()
                } catch (e: TextToSpeechSynthesisInterruptedError) {
                    Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            binding.btnFinish.setOnClickListener {
                dismiss()

                try {
                    tts.close()
                } catch (e: TextToSpeechSynthesisInterruptedError) {
                    Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}