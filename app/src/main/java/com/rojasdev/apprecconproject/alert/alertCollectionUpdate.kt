package com.rojasdev.apprecconproject.alert

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.controllerCheckBox
import com.rojasdev.apprecconproject.controller.requireInput
import com.rojasdev.apprecconproject.data.dataModel.collectorCollection
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.data.entities.RecollectionEntity
import com.rojasdev.apprecconproject.databinding.AlertCollectionBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class alertCollectionUpdate(
    val PK_ID_Recoleccion: Int,
    val PK_ID_Recolector: Int,
    val Alimentacion: String,
    val Cantidad: Double,
    val nameCollector: String,
    private val onClickListener: (RecollectionEntity) -> Unit,
): DialogFragment() {
    private lateinit var binding: AlertCollectionBinding
    private var settingsId: Int? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertCollectionBinding.inflate(LayoutInflater.from(context))
            val builder = AlertDialog.Builder(requireActivity())
                builder.setView(binding.root)

        animatedAlert.animatedInit(binding.cvRecolector)

        binding.tvDescription.text = nameCollector

        val myListInput = listOf(
            binding.etKg
        )

        if(Alimentacion.equals("yes")){
            binding.cbYes.isChecked = true
        } else {
            binding.cbNo.isChecked = true
        }

        binding.etKg.setText(Cantidad.toString())

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
        val formatDate = "EEEE, MMMM dd 'del' yyyy ' Hora: ' HH:mm:ss"
        val formato = SimpleDateFormat(formatDate, Locale("es", "CO"))
        val dateFormat = formato.format(date)

        val collection = RecollectionEntity(
            PK_ID_Recoleccion ,
            kg.toDouble(),
            dateFormat.toString(),
            "active",
            PK_ID_Recolector,
            settingsId!!
        )

        onClickListener(collection)
    }

}