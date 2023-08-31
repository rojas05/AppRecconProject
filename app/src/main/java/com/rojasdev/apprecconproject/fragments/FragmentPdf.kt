package com.rojasdev.apprecconproject.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.rojasdev.apprecconproject.alert.alert_create_pdf
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.databinding.FragmentPdfBinding
import java.io.File

class FragmentPdf : Fragment() {
    private var _binding: FragmentPdfBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPdfBinding.inflate(inflater,container,false)

        animatedAlert.animatedCv(binding.cv)
        animatedAlert.animatedCv(binding.cv1)
        animatedAlert.animatedCv(binding.cv2)


        //revisando el estado de los permisos
        if(checkPermission()) {
            //si los permisos estan concedidos
            Toast.makeText(requireContext(),"bienvenido", Toast.LENGTH_SHORT).show()
        } else {
            //si los permisos NO estan concedidos los solicitamos
            requestPermissions()
        }

        binding.btIWeek.setOnClickListener {
            //llamada del alert para generara el pdf
            alert_create_pdf(
                //que pfd deve generar
                "week"
            ){
                //abrir el pdf generado
                openPdf(it)
            }.show(parentFragmentManager,"dialog")
        }

        return binding.root
    }


//pedida de permisos
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == 200) {
            if (grantResults.isNotEmpty()) {
                val writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (writeStorage && readStorage) {
                    Toast.makeText(requireContext(), "Permiso concedido", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    //verificacion de permisos
    private fun checkPermission(): Boolean {
        val permission1 =
            ContextCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        val permission2 =
            ContextCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED
    }

    //llamada de permisos
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            200
        )
    }

    //abrir pdf
    private fun openPdf(file: String) {
        Toast.makeText(requireContext(), "Abriendo $file", Toast.LENGTH_LONG).show()

        val pdfFile = File(Environment.getExternalStorageDirectory(), file)
        val selectedUri = FileProvider.getUriForFile(
            requireContext(),
            "com.rojasdev.apprecconproject.fileprovider",
            pdfFile
        )

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(selectedUri, "application/pdf")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        try {
            startActivity(intent)
        } catch (e: Exception) {
            // Maneja la excepción si no hay una aplicación adecuada para abrir el PDF
            e.printStackTrace()
        }
    }


}