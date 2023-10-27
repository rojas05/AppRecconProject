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
import android.widget.Toast
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.alert.alertMessage
import com.rojasdev.apprecconproject.alert.alert_create_pdf
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.databinding.FragmentPdfBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Suppress("DEPRECATION", "PrivatePropertyName", "NAME_SHADOWING")
class FragmentPdf : Fragment() {
    private var _binding: FragmentPdfBinding? = null
    private val binding get() = _binding!!

    private val CREATE_PDF_REQUEST_CODE = 2
    private var reportType: String? = null

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
            reportType = getString(R.string.week)
            createPdf(reportType!!, date)
        }

        binding.btnInforme.setOnClickListener {
            reportType = getString(R.string.month)
            createPdf(reportType!!, date)
        }

        binding.btCreateYear.setOnClickListener{
            reportType = getString(R.string.year)
            createPdf(reportType!!, date)
        }

        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CREATE_PDF_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.also { uri ->
                try {
                    if(reportType != null){
                        alert_create_pdf(reportType!!, uri) {
                            openPdfFromUri(uri)
                        }.show(parentFragmentManager,"dialog")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun createPdf(classPdf: String, date: String) {
        val fileName = "${getString(R.string.report)}_$classPdf($date).pdf"

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
    @SuppressLint("QueryPermissionsNeeded")
    private fun openPdfFromUri(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        try{
            startActivity(intent)
        } catch (e: ActivityNotFoundException){
            alertMessage(
                getString(R.string.txtMessageOneInstall),
                getString(R.string.txtMessageTwoInstall),
                getString(R.string.btnOpenShop),
                getString(R.string.btnFinish),
                getString(R.string.txtErrorOpen)
            ){
                if(it == "yes"){
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/search?q=lector+pdf&c=apps"))
                    startActivity(intent)
                }
            }.show(parentFragmentManager,"dialog")
        } catch (e: SecurityException){
            Toast.makeText(context, getString(R.string.txtNotPermissions), Toast.LENGTH_SHORT).show()
        }
    }

    // clean memory
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}