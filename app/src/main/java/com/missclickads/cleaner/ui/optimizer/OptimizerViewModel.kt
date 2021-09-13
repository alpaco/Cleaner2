package com.missclickads.cleaner.ui.optimizer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.missclickads.cleaner.states.OptimizationStates

class OptimizerViewModel : ViewModel() {

    private val _viewStates = MutableLiveData<OptimizationStates>()
    val viewStates = _viewStates
}