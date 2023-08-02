package com.rojasdev.apprecconproject.alert

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.IntentSender.OnFinished
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.BaseTransientBottomBar.ContentViewCallback
import com.rojasdev.apprecconproject.ActivityRecolection
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.customSnackbar
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

        binding.btnClose.setOnClickListener{
            finished()
            dismiss()
        }

        binding.yesAddRecolector.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                finish()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                addCollector()
            }

            override fun afterTextChanged(s: Editable?) {
               if (binding.yesAddRecolector.text!!.isEmpty()){
                   finish()
               }else{
                   addCollector()
               }
            }

        })

        finish()

        val dialog = builder.create()
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                return dialog
    }

    private fun dates(view: View) {

        insertCollector = true
        val recolector = binding.yesAddRecolector.text.toString()
        val addUser = RecolectoresEntity(
            null,
            recolector,
            "active"
        )
        customSnackbar.showCustomSnackbar(view,"Recolector ${recolector} guardado")
        onClickListener(addUser)
    }

    private fun allUser() {
        if (insertCollector) {
            finished()
            dismiss()
        } else {
            dismiss()
        }
    }

    private fun addCollector(){
        binding.btAddRecolector.text = getString(R.string.btnAddRecolector)
        val myListInput = listOf(
            binding.yesAddRecolector
        )

        binding.btAddRecolector.setOnClickListener {
            val required = requireInput.validate(myListInput,requireContext())
            if (required){
                dates(it)
                binding.yesAddRecolector.setText("")
            }
        }
    }
    private fun finish(){
        binding.btAddRecolector.text = getString(R.string.finish)
        binding.btAddRecolector.setOnClickListener {
            allUser()
        }
    }

}
