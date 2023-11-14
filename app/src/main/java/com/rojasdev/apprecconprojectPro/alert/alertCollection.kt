package com.rojasdev.apprecconprojectPro.alert

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconprojectPro.databinding.AlertCollectionBinding
import com.rojasdev.apprecconprojectPro.controller.animatedAlert
import com.rojasdev.apprecconprojectPro.controller.controllerCheckBox
import com.rojasdev.apprecconprojectPro.controller.requireInput
import com.rojasdev.apprecconprojectPro.data.entities.RecolectoresEntity
import com.rojasdev.apprecconprojectPro.data.entities.RecollectionEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
                }
            }
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }



    private fun dates() {
        dismiss()
        val kg = binding.etKg.text.toString()
        val date = Calendar.getInstance().time
        val formatDate = "EEEE MMMM dd 'del' yyyy '  Hora: ' HH:mm:ss"
        val formato = SimpleDateFormat(formatDate, Locale("es", "CO"))
        val dateFormat = formato.format(date)

        /*val collection = RecollectionEntity(
            null,
            kg.toDouble(),
            dateFormat.toString(),
            "active",
            collector.id!!,
            settingsId!!
        )*/

       // onClickListener(collection)
    }
}