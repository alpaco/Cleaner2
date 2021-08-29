package com.missclickads.cleaner.ui.batterysaver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
        val textView: TextView = root.findViewById(R.id.text_notifications)
        batterySaverViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}