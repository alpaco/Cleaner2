package com.missclickads.cleaner.ui.result

import android.graphics.LinearGradient
import android.graphics.Shader
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.missclickads.cleaner.MainActivity
import com.missclickads.cleaner.R

class ResultFragment : Fragment() {

    companion object {
        fun newInstance() = ResultFragment()
    }

    private lateinit var viewModel: ResultViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.result_fragment, container, false)

        val textCount = view.findViewById<TextView>(R.id.textCountOptimized)

        val paint = textCount.paint
        val width = paint.measureText(textCount.text.toString())
        val textShader: Shader = LinearGradient(0f, 0f, width, textCount.textSize, intArrayOf(
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_end) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_middle) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start)
        ), null, Shader.TileMode.CLAMP)
        textCount.paint.setShader(textShader)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ResultViewModel::class.java)
        // TODO: Use the ViewModel
    }

}