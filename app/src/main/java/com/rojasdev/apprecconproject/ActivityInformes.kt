package com.rojasdev.apprecconproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.rojasdev.apprecconproject.databinding.ActivityInformesBinding
import com.rojasdev.apprecconproject.fragments.FragmentInforme
import com.rojasdev.apprecconproject.fragments.FragmentPdf

class ActivityInformes : AppCompatActivity() {
    lateinit var binding: ActivityInformesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityInformesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = "Informe rapido"
        openFragment(FragmentInforme())
        Toast.makeText(this, "list", Toast.LENGTH_SHORT).show()
        binding.bottonNavigationView.setOnNavigationItemSelectedListener {
            meniItem ->
            when(meniItem.itemId){
                R.id.list ->{
                    title = "Informe rapido"
                    openFragment(FragmentInforme())
                    true
                }
                R.id.pdf ->{
                    title = "Informes en PDF"
                    openFragment(FragmentPdf())
                    true
                }
                else -> false
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.ViewPager, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}