package com.missclickads.cleaner.ui.batterysaver

import android.animation.ObjectAnimator
import android.content.ContentValues
import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.missclickads.cleaner.App
import com.missclickads.cleaner.MainActivity
import com.missclickads.cleaner.R
import com.missclickads.cleaner.base.BaseFragment
import com.missclickads.cleaner.databinding.FragmentBatterySaverBinding
import com.missclickads.cleaner.databinding.FragmentJunkCleanerBinding
import com.missclickads.cleaner.states.OptimizationStates
import com.missclickads.cleaner.ui.junkcleaner.JunkCleanerViewModel
import com.missclickads.cleaner.ui.result.FROM
import com.missclickads.cleaner.utils.Screen
import java.lang.Exception
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class BatterySaverFragmentRefactor : BaseFragment(){

    private val viewModel : BatterySaverViewModel by viewModels()
    private var _binding: FragmentBatterySaverBinding? = null
    private val binding get() = _binding!!

    var mInterstitialAd: InterstitialAd? = null
    var act : MainActivity? = null
    var boolForAnim by Delegates.notNull<Boolean>()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBatterySaverBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        act = activity as MainActivity
        boolForAnim = act!!.optimizedBS
        act!!.unblockAllExcept(Screen.BATTERY_SAVER)
        //Get Ad Request
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        val bm = (activity as MainActivity).getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val batteryInfo = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)

        viewModel.viewStates.observe(viewLifecycleOwner){ state ->
            when(state){
                is OptimizationStates.NotOptimize -> {

                    binding.apply {
                        configAd(adRequest, {
                            mInterstitialAd = it
                        }, { navigate() })
                        configGradients(0)
                        textProcess.text= "$batteryInfo %"
                        textTime.text = (batteryInfo * 4 / 60).toString() + " h " + (batteryInfo *4 % 60).toString()+ " m"

                        Log.e("asadsadad","SAFDsadf")
                    }
                    if (act!!.optimizedBS) viewModel.endOptimization()
                    //Btn shake
                    else shakeAnim(boolForAnim, binding.btnOptimize)
                    //todo before optimize
                }
                is OptimizationStates.Optimization -> {

                    binding.apply {

                        btnOptimize.text = "Optimizing..."

                        configGradients(0)
                        (activity as MainActivity).viewPager?.isUserInputEnabled = false
                        val animation = ObjectAnimator.ofInt(progressBarCircle, "progress", 0, 100)
                        progressBarCircle.visibility = View.VISIBLE
                        animation.duration = 5 * 1000
                        animation.start()
                        textProcess.visibility = View.INVISIBLE
                        textProgressProc.visibility = View.VISIBLE
                        boolForAnim = true
                        (activity as MainActivity).offBottomBar()

                        imageView.setImageResource(R.drawable.ellipse_blue)
                        btnOptimize.isClickable = false
                        btnOptimize.setTextColor(ContextCompat.getColor((activity as MainActivity), R.color.gray))
                        btnOptimize.setBackgroundDrawable(activity?.resources?.getDrawable(R.drawable.ic_gradient_blue_dark))

                        Handler().postDelayed({
                            viewModel.endOptimization()
                            showAd()
                        }, (5 * 1000).toLong())
                        setUpBlueGradient(textProgressProc)
                    }
                    progressText(binding.textProgressProc)
                    //todo optimization in process
                }
                is OptimizationStates.Optimized -> {
                    binding.apply {

                        act?.viewPager?.isUserInputEnabled = true
                        btnOptimize.text = "Optimized"
                        configGradients(1)
                        textTime.text = (batteryInfo * 1.2 * 4 / 60).toInt().toString() + " h " + (batteryInfo * 1.2 * 4 % 60).toInt().toString()+ " m"
                        btnOptimize.isClickable = true

                        btnOptimize.setBackgroundDrawable(activity?.resources?.getDrawable(R.drawable.ic_gradient_blue))
                        textProcess.visibility = View.VISIBLE
                        imageView.setImageResource(R.drawable.ellipse_blue)
                        //todo change button back
                        act?.optimizedBS = true
                        act?.optimizeSmth(Screen.BATTERY_SAVER)
                        progressBarCircle.visibility = View.GONE
                        textProgressProc.visibility = View.INVISIBLE
                        btnOptimize.setTextColor(ContextCompat.getColor(act!!, R.color.white))
                        act?.onBottomBar()
                        act?.navigationView?.menu?.findItem(R.id.navigation_battery_saver)?.isEnabled = false

                    }
                    //todo optimize end
                }
                is OptimizationStates.Error -> {
                    //todo error
                    Log.e("Error state", state.err)
                }
            }
        }
        binding.btnOptimize.setOnClickListener {
            viewModel.startOptimization()
        }
    }
    //0 - orange, 1 - blue
    fun configGradients(id : Int){
        binding.apply {
            //Set gradients to text
            if(id == 0){
                setUpOrangeGradient(textProcess)
                setUpOrangeGradient(textTime)

            } else {
                setUpBlueGradient(textProcess)
                setUpBlueGradient(textTime)
            }

        }
    }

    fun showAd(){
        if (mInterstitialAd != null && App.isActivityVisible) {
            mInterstitialAd?.show(activity as MainActivity)
            println("Ads go!")
        }
        else{
            navigate()
        }
    }

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).navigationView?.menu?.findItem(R.id.navigation_battery_saver)?.isEnabled = true
    }

    override fun onResume() {
        super.onResume()
        act!!.unblockAllExcept(Screen.BATTERY_SAVER)
    }

    fun navigate(){
        findNavController().navigate(R.id.resultFragment,Bundle().apply { putString(FROM,"Battery Saver") })
    }

}