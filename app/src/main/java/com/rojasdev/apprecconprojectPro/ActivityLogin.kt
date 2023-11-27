package com.rojasdev.apprecconprojectPro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rojasdev.apprecconprojectPro.databinding.ActivityLoginBinding
import com.rojasdev.apprecconprojectPro.fragments.FragmentRegisterUser


class ActivityLogin : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        firebaseAnalytics = Firebase.analytics
        firebaseAuth = Firebase.auth
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnRegisterUser.setOnClickListener {
            binding.cvWelcome.visibility = View.GONE
            binding.ViewFragmentRegister.visibility = View.VISIBLE
            openFragment(FragmentRegisterUser())
        }

    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.ViewFragmentRegister, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}