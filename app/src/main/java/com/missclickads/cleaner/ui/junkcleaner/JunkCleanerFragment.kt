package com.missclickads.cleaner.ui.junkcleaner

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.LinearGradient
import android.graphics.Shader
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.missclickads.cleaner.MainActivity
import com.missclickads.cleaner.R
import com.missclickads.cleaner.ui.junkcleaner.JunkCleanerViewModel
import com.missclickads.cleaner.utils.Screen

class JunkCleanerFragment : Fragment() {


    private lateinit var viewModel: JunkCleanerViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_junk_cleaner, container, false)
    }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val usageMemory = (150..500).random()

        val textResult = view.findViewById<TextView>(R.id.text_cleaning_required)
        val textResult2 = view.findViewById<TextView>(R.id.text_cleaning_required2)
        val textResult3 = view.findViewById<TextView>(R.id.text_cleaning_required3)
        val textResult4 = view.findViewById<TextView>(R.id.text_cleaning_required4)
        val imageCircle = view.findViewById<ImageView>(R.id.imageView)
        val btnOptimize = view.findViewById<Button>(R.id.btn_optimize)
        val textResult5 = view.findViewById<TextView>(R.id.text_process)
        val imageOk = view.findViewById<ImageView>(R.id.image_ok)
        val progressBarCircle = view.findViewById<ProgressBar>(R.id.progressBarCircle)
        val progressProc = view.findViewById<TextView>(R.id.text_progressproc)
        textResult5.text = "$usageMemory MB"

        val paint = textResult5.paint
        val width = paint.measureText(textResult5.text.toString())
        val textShader: Shader = LinearGradient(0f, 0f, width, textResult5.textSize, intArrayOf(
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_start) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_middle) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_end)
        ), null, Shader.TileMode.CLAMP)
        textResult5.paint.setShader(textShader)

        val paint2 = textResult.paint
        val width2 = paint2.measureText(textResult.text.toString())
        val textShader2: Shader = LinearGradient(0f, 0f, width2, textResult.textSize, intArrayOf(
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_start) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_middle) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_end)
        ), null, Shader.TileMode.CLAMP)
        textResult.paint.setShader(textShader2)

        val paint3 = textResult2.paint
        val width3 = paint3.measureText(textResult2.text.toString())
        val textShader3: Shader = LinearGradient(0f, 0f, width3, textResult2.textSize, intArrayOf(
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_start) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_middle) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_end)
        ), null, Shader.TileMode.CLAMP)
        textResult2.paint.setShader(textShader3)

        val paint4 = textResult3.paint
        val width4 = paint4.measureText(textResult3.text.toString())
        val textShader4: Shader = LinearGradient(0f, 0f, width4, textResult3.textSize, intArrayOf(
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_start) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_middle) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_end)
        ), null, Shader.TileMode.CLAMP)
        textResult3.paint.setShader(textShader4)

        val paint5 = textResult4.paint
        val width5 = paint5.measureText(textResult4.text.toString())
        val textShader5: Shader = LinearGradient(0f, 0f, width5, textResult4.textSize, intArrayOf(
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_start) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_middle) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_end)
        ), null, Shader.TileMode.CLAMP)
        textResult4.paint.setShader(textShader5)

        fun optimized() {

            btnOptimize.text = "Optimized"
            btnOptimize.setBackgroundDrawable(activity?.resources?.getDrawable(R.drawable.ic_gradient_blue))
            textResult.text = "cleared"
            textResult2.text = "cleared"
            textResult3.text = "cleared"
            textResult4.text = "cleared"
            imageCircle.setImageResource(R.drawable.ellipse_blue)

            textResult.setTextColor(ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start))
            textResult2.setTextColor(ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start))
            textResult3.setTextColor(ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start))
            textResult4.setTextColor(ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start))
            imageCircle.setImageResource(R.drawable.ellipse_blue)
            textResult5.visibility = View.INVISIBLE
            imageOk.visibility = View.VISIBLE
            (activity as MainActivity).optimizedJC = true
            (activity as MainActivity).optimizeSmth(Screen.JUNK_CLEANER)
            progressBarCircle.visibility = View.GONE
            progressProc.visibility = View.INVISIBLE
            btnOptimize.setTextColor(ContextCompat.getColor((activity as MainActivity), R.color.white))
            val paint = textResult5.paint
            val width = paint.measureText(textResult5.text.toString())
            val textShader: Shader = LinearGradient(0f, 0f, width, textResult5.textSize, intArrayOf(
                ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_end) ,
                ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_middle) ,
                ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start)
            ), null, Shader.TileMode.CLAMP)
            textResult5.paint.setShader(textShader)

            val paint2 = textResult.paint
            val width2 = paint2.measureText(textResult.text.toString())
            val textShader2: Shader = LinearGradient(0f, 0f, width2, textResult.textSize, intArrayOf(
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_end) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_middle) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start)
        ), null, Shader.TileMode.CLAMP)
        textResult.paint.setShader(textShader2)

        val paint3 = textResult2.paint
        val width3 = paint3.measureText(textResult2.text.toString())
        val textShader3: Shader = LinearGradient(0f, 0f, width3, textResult2.textSize, intArrayOf(
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_end) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_middle) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start)
        ), null, Shader.TileMode.CLAMP)
        textResult2.paint.setShader(textShader3)

        val paint4 = textResult3.paint
        val width4 = paint4.measureText(textResult3.text.toString())
        val textShader4: Shader = LinearGradient(0f, 0f, width4, textResult3.textSize, intArrayOf(
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_end) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_middle) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start)
        ), null, Shader.TileMode.CLAMP)
        textResult3.paint.setShader(textShader4)

        val paint5 = textResult4.paint
        val width5 = paint5.measureText(textResult4.text.toString())
        val textShader5: Shader = LinearGradient(0f, 0f, width5, textResult4.textSize, intArrayOf(
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_end) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_middle) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start)
        ), null, Shader.TileMode.CLAMP)
        textResult4.paint.setShader(textShader5)
        }
        if ((activity as MainActivity).optimizedJC) optimized()
        btnOptimize.setOnClickListener {
            if (!(activity as MainActivity).optimizedJC) {
                btnOptimize.text = "Optimizing..."
                textResult.text = "cleaning..."
                textResult2.text = "cleaning..."
                textResult3.text = "cleaning..."
                textResult4.text = "cleaning..."
                textResult5.visibility = View.INVISIBLE
                progressProc.visibility = View.VISIBLE
                btnOptimize.isClickable = false
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

                        val paint2 = textResult.paint
        val width2 = paint2.measureText(textResult.text.toString())
        val textShader2: Shader = LinearGradient(0f, 0f, width2, textResult.textSize, intArrayOf(
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_start) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_middle) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_end)
        ), null, Shader.TileMode.CLAMP)
        textResult.paint.setShader(textShader2)

        val paint3 = textResult2.paint
        val width3 = paint3.measureText(textResult2.text.toString())
        val textShader3: Shader = LinearGradient(0f, 0f, width3, textResult2.textSize, intArrayOf(
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_start) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_middle) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_end)
        ), null, Shader.TileMode.CLAMP)
        textResult2.paint.setShader(textShader3)

        val paint4 = textResult3.paint
        val width4 = paint4.measureText(textResult3.text.toString())
        val textShader4: Shader = LinearGradient(0f, 0f, width4, textResult3.textSize, intArrayOf(
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_start) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_middle) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_end)
        ), null, Shader.TileMode.CLAMP)
        textResult3.paint.setShader(textShader4)

        val paint5 = textResult4.paint
        val width5 = paint5.measureText(textResult4.text.toString())
        val textShader5: Shader = LinearGradient(0f, 0f, width5, textResult4.textSize, intArrayOf(
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_start) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_middle) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_end)
        ), null, Shader.TileMode.CLAMP)
        textResult4.paint.setShader(textShader5)



                for( i in 0..99){
                    Handler().postDelayed({
                        if (i == 50) {

                            val paint = progressProc.paint
                            val width = paint.measureText(progressProc.text.toString())
                            val textShader: Shader = LinearGradient(0f, 0f, width, progressProc.textSize, intArrayOf(
                                ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_end) ,
                                ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_middle) ,
                                ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start)
                            ), null, Shader.TileMode.CLAMP)
                            progressProc.paint.setShader(textShader)

                                    val paint2 = textResult.paint
        val width2 = paint2.measureText(textResult.text.toString())
        val textShader2: Shader = LinearGradient(0f, 0f, width2, textResult.textSize, intArrayOf(
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_end) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_middle) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start)
        ), null, Shader.TileMode.CLAMP)
        textResult.paint.setShader(textShader2)

        val paint3 = textResult2.paint
        val width3 = paint3.measureText(textResult2.text.toString())
        val textShader3: Shader = LinearGradient(0f, 0f, width3, textResult2.textSize, intArrayOf(
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_end) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_middle) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start)
        ), null, Shader.TileMode.CLAMP)
        textResult2.paint.setShader(textShader3)

        val paint4 = textResult3.paint
        val width4 = paint4.measureText(textResult3.text.toString())
        val textShader4: Shader = LinearGradient(0f, 0f, width4, textResult3.textSize, intArrayOf(
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_end) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_middle) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start)
        ), null, Shader.TileMode.CLAMP)
        textResult3.paint.setShader(textShader4)

        val paint5 = textResult4.paint
        val width5 = paint5.measureText(textResult4.text.toString())
        val textShader5: Shader = LinearGradient(0f, 0f, width5, textResult4.textSize, intArrayOf(
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_end) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_middle) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start)
        ), null, Shader.TileMode.CLAMP)
        textResult4.paint.setShader(textShader5)
                        }
                        progressProc.text = "$i %"
                    }, (i * 50).toLong())
                }

            }
        }



    }
}
//ldas