package com.rojasdev.apprecconprojectPro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.rojasdev.apprecconprojectPro.alert.alertPhoneVerification
import com.rojasdev.apprecconprojectPro.databinding.FragmentRegistrerUserBinding

class FragmentRegisterUser : Fragment() {

    private var _binding: FragmentRegistrerUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrerUserBinding.inflate(inflater, container, false)

        requireActivity().title = "Registar Usuario"

        val phoneNumber = binding.hitPhone.text
        val userName = binding.hitUserName.text
        val password = binding.hitPassword.text

        binding.btnCreateAccount.setOnClickListener {

            if (userName!!.isNotEmpty() && phoneNumber!!.isNotEmpty() && password!!.isNotEmpty()) {
                alertVerificationCodeSMS(userName.toString(), phoneNumber.toString(), password.toString())
            } else {
                Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }

        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun alertVerificationCodeSMS(userName: String, phoneNumber: String, password: String){
        alertPhoneVerification(
            userName,
            "+57 $phoneNumber",
            password
        ).show(parentFragmentManager, "dialog")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}