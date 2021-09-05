package com.missclickads.cleaner.ui.optimizer

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.missclickads.cleaner.MainActivity
import com.missclickads.cleaner.R
import com.missclickads.cleaner.utils.Screen

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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textResult = view.findViewById<TextView>(R.id.text_process)
        val imageCircle = view.findViewById<ImageView>(R.id.imageView)
        val btnOptimize = view.findViewById<Button>(R.id.btn_optimize)

        //before optimize
        val temp = (40..50).random()
        //after optimize
        val tempAfter = (temp * 0.85).toInt()
        fun optimized() {
            btnOptimize.text = "Optimized"
            btnOptimize.setBackgroundDrawable(activity?.resources?.getDrawable(R.drawable.ic_gradient_blue))
            textResult.text = "$tempAfter °C"
            imageCircle.setImageResource(R.drawable.ellipse_blue)
            textResult.setTextColor(
                ContextCompat.getColor(
                    (activity as MainActivity),
                    R.color.gradient_blue_start
                )
            )
            imageCircle.setImageResource(R.drawable.ellipse_blue)
            (activity as MainActivity).optimizedOpt = true
            (activity as MainActivity).optimizeSmth(Screen.OPTIMIZER)
        }
            if ((activity as MainActivity).optimizedOpt) optimized()
            btnOptimize.setOnClickListener {
                if (!(activity as MainActivity).optimizedOpt) {
                    btnOptimize.text = "Optimizing..."
                    btnOptimize.setBackgroundDrawable(activity?.resources?.getDrawable(R.drawable.ic_gradient_blue_dark))
                    Handler().postDelayed({ optimized() }, (5 * 1000).toLong())

                }
            }



    }
}