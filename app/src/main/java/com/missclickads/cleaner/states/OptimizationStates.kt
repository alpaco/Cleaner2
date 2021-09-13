package com.missclickads.cleaner.states

sealed class OptimizationStates {
    object NotOptimize : OptimizationStates()
    object Optimization : OptimizationStates()
    object Optimized : OptimizationStates()
    class Error(val err : String) : OptimizationStates()
}
