package com.missclickads.cleaner.ui.batterysaver

import android.annotation.SuppressLint
import android.content.Context.BATTERY_SERVICE
import android.os.BatteryManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.missclickads.cleaner.MainActivity
import com.missclickads.cleaner.R

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
        textbattery.text ="$batteryInfo %"

        //before optimize
        val batteryhours = batteryInfo * 4 /60
        val batteryminutes = batteryInfo *4 % 60

        //after optimize

        val batteryhoursafter = (batteryInfo * 1.2 * 4 / 60).toInt()
        val batteryminutesafter = (batteryInfo * 1.2 * 4 % 60).toInt()


        textTime.text="$batteryhours h $batteryminutes m"
        btnOptimize.setOnClickListener {
            textTime.text = "$batteryhoursafter h $batteryminutesafter m"
            textResult.visibility = View.INVISIBLE
            imageOk.visibility = View.VISIBLE
            imageCircle.setImageResource(R.drawable.ellipse_blue)
        }

    }
}



