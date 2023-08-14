package com.rojasdev.apprecconproject.alert

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.app.Dialog
import android.content.IntentSender.OnFinished
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.databinding.AlertCreatePdfBinding
import com.rojasdev.apprecconproject.databinding.AlertWelcomeBinding
import com.rojasdev.apprecconproject.pdf.generatePdfSemanal
import java.io.File

class alert_create_pdf(
    var pdf: String,
    var finished: (String) -> Unit
): DialogFragment() {
    private lateinit var binding: AlertCreatePdfBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = AlertCreatePdfBinding.inflate(LayoutInflater.from(context))

        animatedAlert.animatedInit(binding.cvWelcome)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        val animator = ObjectAnimator.ofInt(binding.progressBar, "progress", 0, 100)
        animator.duration = 5000
        animator.start()

        binding.progressBar.isIndeterminate = true

        when (pdf) {
            "year" -> {
                //GENERAR PDF ANUAL
                binding.textView.text = ""
            }
            "month" -> {
                //GENERAR PDF MENSUAL
                binding.textView.text = ""
            }
            else -> {
                binding.textView.text = getString(R.string.weekLoadingPdf)
                starTimer {
                    //llamada para generar el pdf
                    generatePdfSemanal(requireContext(),resources){
                        dismiss()
                        //it correcponde a la ruta en la cual esta el pdf
                        finished(it)
                    }.generate()
                }
            }
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        animatedAlert.onBackAlert(dialog,requireContext(),"")
        return dialog
    }

    fun starTimer(ready : () -> Unit) {
        object: CountDownTimer(900,1){
            override fun onTick(p0: Long) {
            }
            override fun onFinish() {
               ready()
            }
        }.start()
    }
}