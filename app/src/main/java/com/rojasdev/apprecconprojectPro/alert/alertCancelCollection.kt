package com.rojasdev.apprecconprojectPro.alert

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconprojectPro.databinding.AlertCancelCollectionBinding
import com.rojasdev.apprecconprojectPro.adapters.adapterRvcancelCollection
import com.rojasdev.apprecconprojectPro.controller.animatedAlert
import com.rojasdev.apprecconprojectPro.controller.price
import com.rojasdev.apprecconprojectPro.data.dataModel.collecionTotalCollector
import com.rojasdev.apprecconprojectPro.data.dataModel.collectorCollection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class alertCancelCollection (
    var collectionTotal: List<collecionTotalCollector>,
    val collection: List<collectorCollection>,
    var onClickListener: (Int) -> Unit
): DialogFragment() {
    private lateinit var adapter: adapterRvcancelCollection
    private lateinit var binding: AlertCancelCollectionBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertCancelCollectionBinding.inflate(LayoutInflater.from(context))

        animatedAlert.animatedInit(binding.cvRecolector)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        binding.tvNameCollector.text = collectionTotal[0].name_recolector


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

        binding.tvKg.text = "${collectionTotal[0].kg_collection}Kg"
        price.priceSplit(collectionTotal[0].price_total.toInt()){
            binding.tvTotalPrice.text = it
        }

        dates()

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }
    private fun dates() {
        CoroutineScope(Dispatchers.IO).launch{
            launch(Dispatchers.Main) {
                adapter = adapterRvcancelCollection(collection)
                binding.rv.adapter = adapter
                binding.rv.layoutManager = LinearLayoutManager(requireContext())

            }
        }
    }
}