package com.rojasdev.apprecconprojectPro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rojasdev.apprecconprojectPro.databinding.FragmentPdfBinding
import com.rojasdev.apprecconprojectPro.controller.animatedAlert
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

        return binding.root
    }

}