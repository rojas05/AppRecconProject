package com.rojasdev.apprecconproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.rojasdev.apprecconproject.controller.animatedAlert
import com.rojasdev.apprecconproject.databinding.FragmentInformeBinding

class FragmentInforme : Fragment() {
    private var _binding: FragmentInformeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInformeBinding.inflate(inflater,container,false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {

            }
        })

        animatedAlert.animatedCv(binding.cardView)

        return binding.root
    }



}