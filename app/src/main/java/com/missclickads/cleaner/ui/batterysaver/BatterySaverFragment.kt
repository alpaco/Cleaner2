package com.missclickads.cleaner.ui.batterysaver

import android.content.Context.BATTERY_SERVICE
import android.os.BatteryManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bm = (activity as MainActivity).getSystemService(BATTERY_SERVICE) as BatteryManager
        val batteryInfo = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

        btnOptimize.setOnClickListener {
            println(batteryInfo)
        }

    }
