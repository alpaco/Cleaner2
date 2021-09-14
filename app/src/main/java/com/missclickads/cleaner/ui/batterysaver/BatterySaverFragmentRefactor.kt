package com.missclickads.cleaner.ui.batterysaver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.missclickads.cleaner.R
import com.missclickads.cleaner.databinding.FragmentBatterySaverBinding
import com.missclickads.cleaner.databinding.FragmentJunkCleanerBinding
import com.missclickads.cleaner.ui.junkcleaner.JunkCleanerViewModel

class BatterySaverFragmentRefactor : Fragment(R.layout.fragment_battery_saver){

    private val viewModel : BatterySaverViewModel by viewModels()
    private var _binding: FragmentBatterySaverBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBatterySaverBinding.inflate(inflater, container, false)
        return binding.root
    }
}