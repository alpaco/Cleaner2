package com.missclickads.cleaner.ui.phonebooster

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.missclickads.cleaner.states.OptimizationStates

class PhoneBoosterViewModel : ViewModel() {

    private val _viewStates = MutableLiveData<OptimizationStates>(OptimizationStates.NotOptimize)
    val viewStates : LiveData<OptimizationStates> = _viewStates

    fun startOptimization(){
        _viewStates.value = OptimizationStates.Optimization
    }

    fun endOptimization(){
        _viewStates.value = OptimizationStates.Optimized
    }

}