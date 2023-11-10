package com.rojasdev.apprecconprojectPro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.rojasdev.apprecconprojectPro.databinding.ActivityInformesBinding
import com.rojasdev.apprecconprojectPro.fragments.FragmentInforme
import com.rojasdev.apprecconprojectPro.fragments.FragmentPdf

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