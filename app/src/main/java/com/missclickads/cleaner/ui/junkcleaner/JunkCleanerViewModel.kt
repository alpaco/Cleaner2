package com.missclickads.cleaner.ui.junkcleaner

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.missclickads.cleaner.states.OptimizationStates

class  JunkCleanerViewModel : ViewModel() {
    private val _viewStates = MutableLiveData<OptimizationStates>()
    val viewStates = _viewStates
}