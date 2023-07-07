package com.rojasdev.apprecconproject.alert

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.IntentSender.OnFinished
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconproject.ActivityRecolection
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.requireInput
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.databinding.AlertRecolectonBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class alertAddRecolector(
    val onClickListener: (RecolectoresEntity) -> Unit,
    val finished: () -> Unit
): DialogFragment() {

    private lateinit var binding: AlertRecolectonBinding
    private var insertCollector = false
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AlertRecolectonBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        animatedAlert.animatedInit(binding.cvRecolector)

        val myListInput = listOf(
            binding.yesAddRecolector
        )

        binding.btAddRecolector.setOnClickListener {
           val required = requireInput.validate(myListInput,requireContext())
                if (required){
                    dates()
                    binding.yesAddRecolector.setText("")
                }
        }

        binding.btnClose.setOnClickListener{
            dismiss()
        }

        binding.btnFinishAdding.setOnClickListener {
            val recolector = binding.yesAddRecolector.text.toString()
                if (recolector != ""){
                    Toast.makeText(requireContext(),"Presiona el botón 'guardar'", Toast.LENGTH_SHORT).show()
                } else {
                    allUser()
                }
        }

        val dialog = builder.create()
              dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    return dialog
    }

    private fun dates() {
        insertCollector = true
        val recolector = binding.yesAddRecolector.text.toString()
        val addUser = RecolectoresEntity(
            null,
            recolector,
            null
        )
            onClickListener(addUser)
    }

    private fun allUser() {
        if (insertCollector) {
            finished()
        } else {
            dismiss()
        }
    }

}
