package com.missclickads.cleaner.ui.optimizer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.missclickads.cleaner.R

class OptimizerFragment : Fragment() {

    private lateinit var optimizerViewModel: OptimizerViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        optimizerViewModel =
                ViewModelProvider(this).get(OptimizerViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_optimizer, container, false)

        return root
    }
}