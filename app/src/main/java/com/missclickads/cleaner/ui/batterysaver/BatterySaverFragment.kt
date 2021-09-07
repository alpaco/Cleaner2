package com.missclickads.cleaner.ui.batterysaver

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context.BATTERY_SERVICE
import android.os.BatteryManager
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
import com.missclickads.cleaner.utils.Screen

class BatterySaverFragment : Fragment() {

    private lateinit var batterySaverViewModel: BatterySaverViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        batterySaverViewModel =
                ViewModelProvider(this).get(BatterySaverViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_battery_saver, container, false)

        return root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bm = (activity as MainActivity).getSystemService(BATTERY_SERVICE) as BatteryManager
        val batteryInfo = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        val textbattery = view.findViewById<TextView>(R.id.text_process)
        val textTime = view.findViewById<TextView>(R.id.text_time)
        val btnOptimize = view.findViewById<Button>(R.id.btn_optimize)
        val imageOk = view.findViewById<ImageView>(R.id.image_ok)
        val imageCircle = view.findViewById<ImageView>(R.id.imageView)
        val textResult = view.findViewById<TextView>(R.id.text_process)
        val progressBarCircle = view.findViewById<ProgressBar>(R.id.progressBarCircle)
        val progressProc = view.findViewById<TextView>(R.id.text_progressproc)
        textbattery.text ="$batteryInfo %"

        //before optimize
        val batteryhours = batteryInfo * 4 /60
        val batteryminutes = batteryInfo *4 % 60

        //after optimize

        val batteryhoursafter = (batteryInfo * 1.2 * 4 / 60).toInt()
        val batteryminutesafter = (batteryInfo * 1.2 * 4 % 60).toInt()
        fun optimized(){
            btnOptimize.text = "Optimized"
            btnOptimize.setBackgroundDrawable(activity?.resources?.getDrawable(R.drawable.ic_gradient_blue))
            textTime.text = "$batteryhoursafter h $batteryminutesafter m"
            textResult.visibility = View.INVISIBLE
            imageOk.visibility = View.VISIBLE
            imageCircle.setImageResource(R.drawable.ellipse_blue)
            textTime.setTextColor(ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start))
            (activity as MainActivity).optimizedBS = true
            (activity as MainActivity).optimizeSmth(Screen.BATTERY_SAVER)
            progressBarCircle.visibility = View.GONE
            progressProc.visibility = View.INVISIBLE
            btnOptimize.setTextColor(ContextCompat.getColor((activity as MainActivity), R.color.white))


        }
        if ((activity as MainActivity).optimizedBS) optimized()
        textTime.text="$batteryhours h $batteryminutes m"
        btnOptimize.setOnClickListener {
            if(!(activity as MainActivity).optimizedBS ){
                btnOptimize.text = "Optimizing..."
                textResult.visibility = View.INVISIBLE
                progressProc.visibility = View.VISIBLE
                btnOptimize.isClickable = false
                val animation = ObjectAnimator.ofInt(progressBarCircle, "progress", 0, 100)
                progressBarCircle.visibility = View.VISIBLE
                animation.duration = 5 * 1000
                animation.start()
                btnOptimize.setTextColor(ContextCompat.getColor((activity as MainActivity), R.color.gray))
                btnOptimize.setBackgroundDrawable(activity?.resources?.getDrawable(R.drawable.ic_gradient_blue_dark))
                Handler().postDelayed({ optimized() }, (5 * 1000).toLong())
                for( i in 0..99){
                    Handler().postDelayed({
                        if (i == 50) progressProc.setTextColor(ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start))
                        progressProc.text = "$i %"
                    }, (i * 50).toLong())
                }
        }

    }
}
}



