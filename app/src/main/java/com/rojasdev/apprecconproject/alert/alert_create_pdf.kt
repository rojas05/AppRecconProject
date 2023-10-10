package com.rojasdev.apprecconproject.alert

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.textToSpeech
import com.rojasdev.apprecconproject.databinding.AlertCreatePdfBinding
import com.rojasdev.apprecconproject.pdf.generateMonthPDF
import com.rojasdev.apprecconproject.pdf.generatePdfSemanal
import com.rojasdev.apprecconproject.pdf.generateYearPDF
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class alert_create_pdf(
    private var pdf: String,
    private var uri: Uri,
    var finished: () -> Unit
): DialogFragment() {
    private lateinit var binding: AlertCreatePdfBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = AlertCreatePdfBinding.inflate(LayoutInflater.from(context))

        animatedAlert.animatedInit(binding.cvWelcome)
        createFolderPermission()

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        val animator = ObjectAnimator.ofInt(binding.progressBar, "progress", 0, 100)
        animator.duration = 5000
        animator.start()

        binding.progressBar.isIndeterminate = true

        when (pdf) {
            "year" -> {
                //GENERAR PDF ANUAL
                CoroutineScope(Dispatchers.IO).launch {
                    textToSpeech().start(
                        requireContext(),
                        getString(R.string.weekLoadingPdf)
                    ){}
                }
                binding.textView.text = getString(R.string.yearLoadingPdf)
                starTimer {
                    generateYearPDF(requireContext(), resources){
                        dismiss()
                        finished()
                    }.generateYearPdf(uri)
                }
            }
            "week" -> {
                //GENERAR PDF MENSUAL
                CoroutineScope(Dispatchers.IO).launch {
                    textToSpeech().start(requireContext(),
                        getString(R.string.weekLoadingPdf)){}
                }
                binding.textView.text = getString(R.string.weekLoadingPdf)
                starTimer {
                    //llamada para generar el pdf
                    generatePdfSemanal(requireContext(), resources){
                        dismiss()
                        //it correcponde a la ruta en la cual esta el pdf
                        finished()
                    }.generate(uri)
                }
            }
            else -> {
                CoroutineScope(Dispatchers.IO).launch {
                    textToSpeech().start(
                        requireContext(),
                        getString(R.string.weekLoadingPdf)
                    ){}
                }
                binding.textView.text = getString(R.string.monthLoadingPdf)
                starTimer {
                    generateMonthPDF(requireContext(), resources){
                        dismiss()
                        finished()
                    }.generatePfd(uri)
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
            override fun onTick(p0: Long) {}
            override fun onFinish() {
               ready()
            }
        }.start()
    }

    @SuppressLint("SetWorldReadable", "SetWorldWritable", "SuspiciousIndentation")
    private fun createFolderPermission() {
        // Ruta donde se creará la carpeta
        val pdfFolderPath = "/Users/Cristian/AndroidStudioProjects/AppRecconProject/mica"

        // Crea la carpeta si no existe
        val pdfFolder = File(pdfFolderPath)
            pdfFolder.mkdirs()

        // Otorga permisos de lectura y escritura a la carpeta
        pdfFolder.setReadable(true, false)
        pdfFolder.setWritable(true, false)

        Toast.makeText(requireContext(), "echo", Toast.LENGTH_SHORT).show()
    }
}