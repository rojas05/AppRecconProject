package com.rojasdev.apprecconproject.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.net.Uri
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.alert.alert_create_pdf
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.controller.customSnackbar
import com.rojasdev.apprecconproject.databinding.FragmentPdfBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FragmentPdf : Fragment() {
    private var _binding: FragmentPdfBinding? = null
    private val binding get() = _binding!!

    private val CREATE_PDF_REQUEST_CODE = 2
    private var clickWeek: Boolean = false
    private var clickMonth: Boolean = false
    private var clickYear: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPdfBinding.inflate(inflater,container,false)

        val calendar = Calendar.getInstance().time
        val format = SimpleDateFormat("yyyy-MM-dd", Locale("es", "CO"))
        val date = format.format(calendar)

        animatedAlert.animatedCv(binding.cv)
        animatedAlert.animatedCv(binding.cv1)
        animatedAlert.animatedCv(binding.cv2)

        binding.btIWeek.setOnClickListener {
            clickWeek = true
            clickMonth = false
            clickYear = false
            createPdf(getString(R.string.week), date)
        }

        binding.btnInforme.setOnClickListener {
            clickMonth = true
            clickWeek = false
            clickYear = false
            createPdf(getString(R.string.month), date)
        }

        binding.btCreateYear.setOnClickListener{
            clickYear = true
            clickMonth = false
            clickWeek = false
            createPdf(getString(R.string.year), date)
        }

        return binding.root
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CREATE_PDF_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.also { uri ->
                try {

                    // Pdf Semanal
                    if(clickWeek){
                        alert_create_pdf(
                            "week",
                            uri
                        ) {
                            openPdfFromUri(uri)
                        }.show(parentFragmentManager,"dialog")
                    }

                    // Pdf Mensual
                    if (clickMonth){
                        alert_create_pdf(
                            "month",
                            uri
                        ) {
                            openPdfFromUri(uri)
                        }.show(parentFragmentManager,"dialog")
                    }

                    // Pdf Anual
                    if (clickYear){
                        alert_create_pdf(
                            "year",
                            uri
                        ) {
                            openPdfFromUri(uri)
                        }.show(parentFragmentManager, "dialog")
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun createPdf(classPdf: String, date: String) {
        val fileName = "informe_$classPdf($date).pdf"

        // Crear una instancia de la class PdfGenerator selecionar donde save
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/pdf"
        intent.putExtra(Intent.EXTRA_TITLE, fileName)

        try {
            startActivityForResult(intent, CREATE_PDF_REQUEST_CODE)
        } catch (e: ActivityNotFoundException){
            e.printStackTrace()
        }
    }

    // Open PDF
    private fun openPdfFromUri(uri: Uri) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(intent)
        } catch (e: ActivityNotFoundException){
            customSnackbar.showCustomSnackbar(binding.textView,"lo sentimos no tienes instalado un lector de pdf")
        }
    }

    // Liberar memoria
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}