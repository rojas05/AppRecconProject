package com.rojasdev.apprecconproject

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.rojasdev.apprecconproject.alert.alertAddRecolector
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.entities.RecolectoresEntity
import com.rojasdev.apprecconproject.databinding.ActivityRecolectionBinding
import com.rojasdev.apprecconproject.fragments.FragmentCollecion
import com.rojasdev.apprecconproject.fragments.FragmentCollectors
import com.rojasdev.apprecconproject.fragments.FragmentInforme
import com.rojasdev.apprecconproject.fragments.FragmentPdf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityRecolection : AppCompatActivity() {
    private lateinit var binding: ActivityRecolectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRecolectionBinding.inflate(layoutInflater)
         super.onCreate(savedInstanceState)
          setContentView(binding.root)

        title = "Recolectores"

        openFragment(FragmentCollectors{
            if (it == "down"){
                hideNavBar()
            }else if (it == "up"){
                appearNavBar()
            }
        })

        binding.floatingActionButton.setOnClickListener {
            alertAddRecolcetor()
        }

    }
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.ViewPagerCollectors, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun hideNavBar(){
        onClickFalse()
        binding.bottomAppBarCollectors.alpha = 0f
        binding.bottonNavigationViewCollectors.alpha = 0f
        binding.floatingActionButton.alpha = 0f
        binding.floatingActionButton.isClickable = false
    }

    private fun appearNavBar(){
        onClickTrue()
        binding.bottomAppBarCollectors.alpha = 1f
        binding.bottonNavigationViewCollectors.alpha = 1f
        binding.floatingActionButton.alpha = 1f
        binding.floatingActionButton.isClickable = true
    }

    private fun onClickFalse(){
        binding.bottonNavigationViewCollectors.setOnNavigationItemSelectedListener {
                meniItem ->
            when(meniItem.itemId){
                R.id.collectors ->{
                    false
                }
                R.id.collection ->{
                    false
                }
                else -> false
            }
        }
    }
    private fun onClickTrue(){
        binding.bottonNavigationViewCollectors.setOnNavigationItemSelectedListener {
                meniItem ->
            when(meniItem.itemId){
                R.id.collectors ->{
                    title = "Recolectores"
                    openFragment(FragmentCollectors{
                        if (it == "down"){
                            hideNavBar()
                        }else if (it == "up"){
                            appearNavBar()
                        }
                    })
                    true
                }
                R.id.collection ->{
                    title = "Recoleccion"
                    openFragment(FragmentCollecion{
                        if (it == "down"){
                            hideNavBar()
                        }else if (it == "up"){
                            appearNavBar()
                        }
                    })
                    true
                }
                else -> false
            }
        }
    }

    private fun alertAddRecolcetor() {
        alertAddRecolector(
            {
                insertRecolector(it)
            },
            {
                openFragment(FragmentCollectors{
                    if (it == "down"){
                        hideNavBar()
                    }else if (it == "up"){
                        appearNavBar()
                    }
                })
            }
        ).show(supportFragmentManager, "dialog")
    }

    private fun insertRecolector(recolector: RecolectoresEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDataBase.getInstance(this@ActivityRecolection).RecolectoresDao().add(recolector)
        }
        Toast.makeText(this, "Se agrego un nuevo miembro ${recolector.name}", Toast.LENGTH_SHORT).show()
    }
}