package com.missclickads.cleaner.ui.phonebooster

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
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
import com.missclickads.cleaner.MainActivity
import com.missclickads.cleaner.utils.Screen
import kotlin.math.ceil
import kotlin.math.roundToInt
import android.animation.ObjectAnimator
import android.content.res.Resources
import android.graphics.LinearGradient
import android.graphics.Shader

import android.graphics.drawable.Drawable
import com.missclickads.cleaner.R


class PhoneBoosterFragment : Fragment() {

    private lateinit var phoneBoosterViewModel: PhoneBoosterViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        phoneBoosterViewModel =
                ViewModelProvider(this).get(PhoneBoosterViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_phone_booster, container, false)
        return root
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).navigationView?.menu?.findItem(R.id.navigation_phone_booster)?.isEnabled = true
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor", "ResourceType", "UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //todo enable other
        //HERE I OFF BUTTOn
        (activity as MainActivity).navigationView?.menu?.findItem(R.id.navigation_phone_booster)?.isEnabled = false

        val actManager = (activity as MainActivity).getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        actManager.getMemoryInfo(memInfo)
        val totalMemory = ceil((memInfo.totalMem / (1024 * 1024)).toDouble() / 1000).toInt()

        //before optimize
        val usageMemoryPercent = (60..95).random()
        val usageMemory = (totalMemory * usageMemoryPercent.toDouble()).roundToInt() / 100.0
        val runningProcess = (1150..1483).random()

        //after optimize
        val usageMemoryPercentAfter = (25..43).random()
        val usageMemoryAfter  = (totalMemory * usageMemoryPercentAfter.toDouble()).roundToInt() / 100.0
        val runningProcessAfter  = (240..470).random()


        val textMemory = view.findViewById<TextView>(R.id.text_ram_usage_help)
        val textResult = view.findViewById<TextView>(R.id.text_process)
        val textRunningProcess = view.findViewById<TextView>(R.id.text_running_processes_value)
        val textPercent = view.findViewById<TextView>(R.id.text_ram_usage_value2)
        val btnOptimize = view.findViewById<Button>(R.id.btn_optimize)
        val imageOk = view.findViewById<ImageView>(R.id.image_ok)
        val imageCircle = view.findViewById<ImageView>(R.id.imageView)
        val progressBarCircle = view.findViewById<ProgressBar>(R.id.progressBarCircle)
        val progressProc = view.findViewById<TextView>(R.id.text_progressProc)
        textRunningProcess.text = "$runningProcess"
        textPercent.text = "$usageMemoryPercent%"
        textMemory.text = "$usageMemory GB / $totalMemory GB"
        textResult.text = "$usageMemory GB"



        val barBot = view.findViewById<ProgressBar>(R.id.progressBar)

        val paint = textRunningProcess.paint
        val width = paint.measureText(textRunningProcess.text.toString())
        val textShader: Shader = LinearGradient(0f, 0f, width, textRunningProcess.textSize, intArrayOf(
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_start) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_middle) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_end)
        ), null, Shader.TileMode.CLAMP)
        textRunningProcess.paint.setShader(textShader)

        val paint2 = textPercent.paint
        val width2 = paint2.measureText(textPercent.text.toString())
        val textShader2: Shader = LinearGradient(0f, 0f, width2, textPercent.textSize, intArrayOf(
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_start) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_middle) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_end)
        ), null, Shader.TileMode.CLAMP)
        textPercent.paint.setShader(textShader2)

        val paint3 = textResult.paint
        val width3 = paint3.measureText(textResult.text.toString())
        val textShader3: Shader = LinearGradient(0f, 0f, width3, textResult.textSize, intArrayOf(
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_start) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_middle) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_end)
        ), null, Shader.TileMode.CLAMP)
        textResult.paint.setShader(textShader3)



        //after optimization
        fun optimized(){
            btnOptimize.text = "Optimized"
            btnOptimize.setBackgroundDrawable(activity?.resources?.getDrawable(R.drawable.ic_gradient_blue))
            textResult.visibility = View.INVISIBLE
            imageOk.visibility = View.VISIBLE
            textRunningProcess.text = "$runningProcessAfter"
            textPercent.text = "$usageMemoryPercentAfter%"
            textMemory.text = "$usageMemoryAfter GB / $totalMemory GB"
            imageCircle.setImageResource(R.drawable.ellipse_blue)
            //todo change button back
            textPercent.setTextColor(ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start))
            textRunningProcess.setTextColor(ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start))
            (activity as MainActivity).optimizedPB = true
            (activity as MainActivity).optimizeSmth(Screen.PHONE_BOOSTER)
            barBot.progress = usageMemoryPercentAfter
            progressBarCircle.visibility = View.GONE
            barBot.progressDrawable = resources.getDrawable(R.drawable.progress_bar_hor_blue)
            progressProc.visibility = View.INVISIBLE
            btnOptimize.setTextColor(ContextCompat.getColor((activity as MainActivity), R.color.white))

            val paint = textRunningProcess.paint
            val width = paint.measureText(textRunningProcess.text.toString())
            val textShader: Shader = LinearGradient(0f, 0f, width, textRunningProcess.textSize, intArrayOf(
                ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_end) ,
                ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_middle) ,
                ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start)
            ), null, Shader.TileMode.CLAMP)
            textRunningProcess.paint.setShader(textShader)

            val paint2 = textPercent.paint
            val width2 = paint2.measureText(textPercent.text.toString())
            val textShader2: Shader = LinearGradient(0f, 0f, width2, textPercent.textSize, intArrayOf(
                ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_end) ,
                ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_middle) ,
                ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start)
            ), null, Shader.TileMode.CLAMP)
            textPercent.paint.setShader(textShader2)

            val paint3 = textResult.paint
            val width3 = paint3.measureText(textResult.text.toString())
            val textShader3: Shader = LinearGradient(0f, 0f, width3, textResult.textSize, intArrayOf(
                ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_end) ,
                ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_middle) ,
                ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start)
            ), null, Shader.TileMode.CLAMP)
            textResult.paint.setShader(textShader3)
        }
        barBot.progress = usageMemoryPercent
        if ((activity as MainActivity).optimizedPB) optimized()

//        bar.progressDrawable =  activity?.resources?.getDrawable(R.drawable.ic_gradient_blue)
//        bar.setProgressDrawableTiled(activity?.resources?.getDrawable(R.drawable.ic_gradient_orange))


        btnOptimize.setOnClickListener {
            if(!(activity as MainActivity).optimizedPB ){
                btnOptimize.text = "Optimizing..."
                val animation = ObjectAnimator.ofInt(progressBarCircle, "progress", 0, 100)
                progressBarCircle.visibility = View.VISIBLE
                animation.duration = 5 * 1000
                animation.start()
                textResult.visibility = View.INVISIBLE
                progressProc.visibility = View.VISIBLE
                imageCircle.setImageResource(R.drawable.ellipse_blue)
                btnOptimize.isClickable = false
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
                        if (i == 50) {val paint = progressProc.paint
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

//    fun doNotClickYourSelf(){
//
//    }
}