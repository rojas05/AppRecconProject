package com.rojasdev.apprecconproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.databinding.ActivityInformesBinding
import com.rojasdev.apprecconproject.fragments.FragmentHistoryReport
import com.rojasdev.apprecconproject.fragments.FragmentInforme
import com.rojasdev.apprecconproject.fragments.FragmentPdf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityInformes : AppCompatActivity() {

    lateinit var binding: ActivityInformesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityInformesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = "Informe rapido"
        openFragment(FragmentInforme())

        val menuItem = binding.bottonNavigationView.menu.findItem(R.id.btnHistory)
        CoroutineScope(Dispatchers.IO).launch {
            val getHistory = AppDataBase.getInstance(this@ActivityInformes).ReportHistoryDao().showAll()
            launch(Dispatchers.Main) {
                menuItem.isVisible = getHistory.isNotEmpty()
            }
        }

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
                R.id.btnHistory ->{
                    title = "Historial de informes"
                    openFragment(FragmentHistoryReport())
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