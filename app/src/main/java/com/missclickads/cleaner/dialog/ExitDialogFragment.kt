package com.missclickads.cleaner.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.BatteryManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.missclickads.cleaner.MainActivity
import com.missclickads.cleaner.R
import com.missclickads.cleaner.databinding.CustomExitDialogBinding
import kotlin.system.exitProcess

class ExitDialogFragment : DialogFragment() {

    var act : MainActivity? = null
    private var _binding: CustomExitDialogBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (getDialog() != null && getDialog()?.getWindow() != null) {
            getDialog()?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            getDialog()?.getWindow()?.requestFeature(Window.FEATURE_NO_TITLE);
        }
        _binding = CustomExitDialogBinding.inflate(inflater, container, false)
        return binding.root
        //return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        act = (activity as MainActivity)
        val btnQuit = view.findViewById<Button>(R.id.btn_quit)
        val bm = (activity as MainActivity).getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val batteryInfo = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        if(!act!!.optimizedBS) {
            binding.apply {
                gradientTextView.text = "Battery Saver still not optimized!"
                gradientTextView2.text = "${batteryInfo} %"
                appCompatButton.text = "Battery Saver"
                appCompatButton.setOnClickListener {
                    act!!.callbackFromDialog(1)
                    dismiss()
                }
            }
        }
        if(!act!!.optimizedJC){
            binding.apply {
                gradientTextView.text = "Junk Cleaner still not optimized!"
                gradientTextView2.text = "${act!!.usageMemoryGeneral} MB"
                appCompatButton.text = "Junk Cleaner"
                appCompatButton.setOnClickListener {
                    act!!.callbackFromDialog(3)
                    dismiss()
                }
            }
        }
    if(!!!act!!.optimizedOpt) {
            binding.apply {
                gradientTextView.text = "CPU Cooler still not optimized!"
                gradientTextView2.text = "${act!!.textResultGeneral}°C"
                appCompatButton.text = "Optimizer"
                appCompatButton.setOnClickListener {
                    act!!.callbackFromDialog(2)
                    dismiss()
                }
            }
        }
        if(!act!!.optimizedPB) {
            binding.apply {
                gradientTextView.text = "Phone Booster still not optimized!"
                gradientTextView2.text = "${act!!.usageMemoryPercentGeneral}%"
                appCompatButton.text = "Phone Booster"
                appCompatButton.setOnClickListener {
                    act!!.callbackFromDialog(0)
                    dismiss()
                }
            }

        }
        btnQuit.setOnClickListener {
             act!!.finish()
        }
    }
}