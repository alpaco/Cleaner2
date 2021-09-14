package com.missclickads.cleaner.ui.result

import android.graphics.LinearGradient
import android.graphics.Shader
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.missclickads.cleaner.MainActivity
import com.missclickads.cleaner.R

const val FROM = "FROM"
const val TO = "TO"
class ResultFragment : Fragment() {

    private var from : String = "Phone Booster"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            from = it.getString(FROM).toString()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.result_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textFrom = view.findViewById<TextView>(R.id.text_fragment_name)
        val textCount = view.findViewById<TextView>(R.id.textCountOptimized)
        val btnPB = view.findViewById<Button>(R.id.appCompatButton)
        val btnBS = view.findViewById<Button>(R.id.appCompatButton2)
        val btnOpt = view.findViewById<Button>(R.id.appCompatButton3)
        val btnJC = view.findViewById<Button>(R.id.appCompatButton4)
        val act = activity as MainActivity
        var completed = 0
        val adRequest = AdRequest.Builder().build()
        act.navigationView?.visibility = View.GONE
        setGradient(textCount,act)
        textFrom.text = from
        if (act.optimizedPB){
            completed += 1
            btnPB.visibility = View.GONE
        }
        if (act.optimizedBS){
            completed += 1
            btnBS.visibility = View.GONE
        }
        if (act.optimizedOpt){
            completed += 1
            btnOpt.visibility = View.GONE
        }
        if (act.optimizedJC){
            completed += 1
            btnJC.visibility = View.GONE
        }
        btnPB.setOnClickListener {
            findNavController().navigate(R.id.mainScreenFragment,Bundle().apply { putInt(TO,0) })
        }
        btnBS.setOnClickListener {
            findNavController().navigate(R.id.mainScreenFragment,Bundle().apply { putInt(TO,1) })
        }
        btnOpt.setOnClickListener {
            findNavController().navigate(R.id.mainScreenFragment,Bundle().apply { putInt(TO,2) })
        }
        btnJC.setOnClickListener {
            findNavController().navigate(R.id.mainScreenFragment,Bundle().apply { putInt(TO,3) })
        }
        textCount.text = if(completed == 4) "You completed all optimizations!"
        else "$completed/4 optimization completed!"
    }

    private fun setGradient(textCount: TextView, act: MainActivity){
        val paint = textCount.paint
        val width = paint.measureText(textCount.text.toString())
        val textShader: Shader = LinearGradient(0f, 0f, width, textCount.textSize, intArrayOf(
            ContextCompat.getColor(act, R.color.gradient_blue_end) ,
            ContextCompat.getColor(act, R.color.gradient_blue_middle) ,
            ContextCompat.getColor(act, R.color.gradient_blue_start)
        ), null, Shader.TileMode.CLAMP)
        textCount.paint.shader = textShader
    }
}