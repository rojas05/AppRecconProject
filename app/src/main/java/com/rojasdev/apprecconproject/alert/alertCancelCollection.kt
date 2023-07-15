package com.rojasdev.apprecconproject.alert

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.adapters.adapterRvColleccionTotal
import com.rojasdev.apprecconproject.adapters.adapterRvcancelCollection
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.controller.requireInput
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.dataModel.collecionTotalCollector
import com.rojasdev.apprecconproject.data.dataModel.collectorCollection
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.data.entities.RecollectionEntity
import com.rojasdev.apprecconproject.databinding.AlertCancelCollectionBinding
import com.rojasdev.apprecconproject.databinding.AlertCollectionBinding
import com.rojasdev.apprecconproject.viewHolders.viewHolderCvCollectionTotal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

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