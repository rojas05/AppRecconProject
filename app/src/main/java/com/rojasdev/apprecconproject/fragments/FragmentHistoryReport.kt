package com.rojasdev.apprecconproject.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.ActivityMainModule
import com.rojasdev.apprecconproject.adapters.adapterRvReportHistory
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.databinding.FragmentHistoryReportBinding
import com.rojasdev.apprecconproject.databinding.FragmentHistoryReportBinding.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentHistoryReport : Fragment() {

    private lateinit var adapter: adapterRvReportHistory
    private var _binding: FragmentHistoryReportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflate(inflater, container, false)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    startActivity(Intent(requireContext(), ActivityMainModule::class.java))
                }
            })

        showAllReport()

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun showAllReport(){
        CoroutineScope(Dispatchers.IO).launch {
            val getReport = AppDataBase.getInstance(requireContext()).ReportHistoryDao().showAll()
            launch(Dispatchers.Main) {
                adapter = adapterRvReportHistory(getReport)
                binding.rvInfoHistory.adapter = adapter
                binding.rvInfoHistory.layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}