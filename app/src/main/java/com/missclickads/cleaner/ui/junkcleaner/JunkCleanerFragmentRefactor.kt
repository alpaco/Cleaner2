package com.missclickads.cleaner.ui.junkcleaner

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.missclickads.cleaner.databinding.FragmentJunkCleanerBinding
import com.missclickads.cleaner.databinding.FragmentOptimizerBinding
import com.missclickads.cleaner.states.OptimizationStates
import com.missclickads.cleaner.ui.optimizer.OptimizerViewModel

class JunkCleanerFragmentRefactor : Fragment() {


    private val viewModel : JunkCleanerViewModel by viewModels()

    private var _binding: FragmentJunkCleanerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJunkCleanerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewStates.observe(viewLifecycleOwner){ state ->
            when(state){
                is OptimizationStates.NotOptimize -> {
                    //todo before optimize
                }
                is OptimizationStates.Optimization -> {
                    //todo optimization in process
                }
                is OptimizationStates.Optimized -> {
                    //todo optimize end
                }
                is OptimizationStates.Error -> {
                    //todo error
                    Log.e("Error state", state.err)
                }
            }
        }
    }
}