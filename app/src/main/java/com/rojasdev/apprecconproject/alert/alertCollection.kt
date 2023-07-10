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
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.controller.requireInput
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.data.entities.RecollectionEntity
import com.rojasdev.apprecconproject.databinding.AlertCollectionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class alertCollection (
    var collector: RecolectoresEntity,
    var onClickListener: (RecollectionEntity) -> Unit
): DialogFragment() {
    private var settingsId: Int? = null
    private lateinit var binding: AlertCollectionBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertCollectionBinding.inflate(LayoutInflater.from(context))

        animatedAlert.animatedInit(binding.cvRecolector)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        val myListInput = listOf(
            binding.etKg
        )

        binding.tvNameCollector.text = collector.name

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

        binding.btReady.setOnClickListener {
            val require = requireInput.validate(myListInput,requireContext())
            if (require){
                checkBoxFun()
            }
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun checkBoxFun() {
        if(binding.cbNo.isChecked){
            CoroutineScope(Dispatchers.IO).launch{
                val query = AppDataBase.getInstance(requireContext()).SettingDao().getAliment("no")
                launch(Dispatchers.Main) {
                    settingsId = query[0].Id
                    dates()
                }
            }
        }else if(binding.cbYes.isChecked){
            CoroutineScope(Dispatchers.IO).launch{
                val query = AppDataBase.getInstance(requireContext()).SettingDao().getAliment("yes")
                launch(Dispatchers.Main) {
                    settingsId = query[0].Id
                    dates()
                }
            }
        }else{
            binding.tvAliment.error = "indique la alimentacion"
            binding.tvAliment.requestFocus()
            requireInput.vibratePhone(requireContext())
        }
    }

    private fun dates() {
        dismiss()
        val kg = binding.etKg.text.toString()
        val date = Date()
        val formato = SimpleDateFormat("dd/MM/yyyy")
        val dateFormat = formato.format(date)

        val collection = RecollectionEntity(
            null,
            kg.toDouble(),
            dateFormat.toString(),
            "active",
            collector.id!!,
            settingsId!!
        )

        onClickListener(collection)
    }
}