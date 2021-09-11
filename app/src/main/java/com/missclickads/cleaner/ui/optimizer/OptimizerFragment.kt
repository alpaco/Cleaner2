package com.missclickads.cleaner.ui.optimizer

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
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
    override fun onPause() {
        super.onPause()
        (activity as MainActivity).navigationView?.menu?.findItem(R.id.navigation_optimizer)?.isEnabled = true
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).navigationView?.menu?.findItem(R.id.navigation_optimizer)?.isEnabled = false
        val textResult = view.findViewById<TextView>(R.id.text_process)
        val imageCircle = view.findViewById<ImageView>(R.id.imageView)
        val btnOptimize = view.findViewById<Button>(R.id.btn_optimize)
        val progressBarCircle = view.findViewById<ProgressBar>(R.id.progressBarCircle)
        val progressProc = view.findViewById<TextView>(R.id.text_progressProc)


        //before optimize
        val temp = (40..50).random()
        //after optimize
        val tempAfter = (temp * 0.85).toInt()



        //telephone's apps info
        val pm: PackageManager = (activity as MainActivity).packageManager
        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        for (packageInfo in packages) {
            Log.d(TAG, "Installed package :" + packageInfo.packageName)
            Log.d(TAG, "Source dir : " + packageInfo.sourceDir)
            Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName))
        }





        val paint = textResult.paint
        val width = paint.measureText(textResult.text.toString())
        val textShader: Shader = LinearGradient(0f, 0f, width, textResult.textSize, intArrayOf(
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_start) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_middle) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_end)

        ), null, Shader.TileMode.CLAMP)
        textResult.paint.setShader(textShader)



        fun optimized() {
            btnOptimize.text = "Optimized"
            btnOptimize.setBackgroundDrawable(activity?.resources?.getDrawable(R.drawable.ic_gradient_blue))
            textResult.text = "$tempAfter°C"
            imageCircle.setImageResource(R.drawable.ellipse_blue)
            textResult.setTextColor(ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start))
            imageCircle.setImageResource(R.drawable.ellipse_blue)
            (activity as MainActivity).optimizedOpt = true
            (activity as MainActivity).optimizeSmth(Screen.OPTIMIZER)
            progressBarCircle.visibility = View.GONE
            progressProc.visibility = View.INVISIBLE
            textResult.visibility = View.VISIBLE
            btnOptimize.setTextColor(ContextCompat.getColor((activity as MainActivity), R.color.white))
            val paint = textResult.paint
            val width = paint.measureText(textResult.text.toString())
            val textShader: Shader = LinearGradient(0f, 0f, width, textResult.textSize, intArrayOf(
                ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_end) ,
                ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_middle) ,
                ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start)

            ), null, Shader.TileMode.CLAMP)
            textResult.paint.setShader(textShader)


            (activity as MainActivity).onBottomBar()
            (activity as MainActivity).navigationView?.menu?.findItem(R.id.navigation_optimizer)?.isEnabled = false
        }
        if ((activity as MainActivity).optimizedOpt) optimized()
        btnOptimize.setOnClickListener {
            if (!(activity as MainActivity).optimizedOpt) {
                btnOptimize.text = "Optimizing..."
                textResult.visibility = View.INVISIBLE
                progressProc.visibility = View.VISIBLE
                btnOptimize.isClickable = false


                (activity as MainActivity).offBottomBar()

                imageCircle.setImageResource(R.drawable.ellipse_blue)
                val animation = ObjectAnimator.ofInt(progressBarCircle, "progress", 0, 100)
                progressBarCircle.visibility = View.VISIBLE
                animation.duration = 5 * 1000
                animation.start()
                btnOptimize.setTextColor(ContextCompat.getColor((activity as MainActivity), R.color.gray))
                btnOptimize.setBackgroundDrawable(activity?.resources?.getDrawable(R.drawable.ic_gradient_blue_dark))
                Handler().postDelayed({ optimized() }, (5 * 1000).toLong())
                val paint = progressProc.paint
                val width = paint.measureText(progressProc.text.toString())
                val textShader: Shader = LinearGradient(0f, 0f, width, progressProc.textSize, intArrayOf(
                    ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_end) ,
                    ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_middle) ,
                    ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start)

                ), null, Shader.TileMode.CLAMP)
                progressProc.paint.setShader(textShader)


                for( i in 0..99){

                    Handler().postDelayed({
                        if (i == 50)
                        {val paint = progressProc.paint
                        val width = paint.measureText(progressProc.text.toString())
                        val textShader: Shader = LinearGradient(0f, 0f, width, progressProc.textSize, intArrayOf(
                            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_end) ,
                            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_middle) ,
                            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start)

                        ), null, Shader.TileMode.CLAMP)
                        progressProc.paint.setShader(textShader) }
                        progressProc.text = "$i %"
                    }, (i * 50).toLong())

                }

                }
            }
    }
}

