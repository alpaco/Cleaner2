package com.missclickads.cleaner.ui.phonebooster

import android.animation.ObjectAnimator
import android.app.ActivityManager
import android.content.ContentValues
import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
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
import com.missclickads.cleaner.databinding.CustomExitDialogBinding
import com.missclickads.cleaner.databinding.FragmentPhoneBoosterBinding
import com.missclickads.cleaner.states.OptimizationStates
import com.missclickads.cleaner.ui.result.FROM
import com.missclickads.cleaner.utils.Screen
import java.lang.Exception
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.ceil
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class PhoneBoosterFragmentRefactor : BaseFragment() {

    private val viewModel : PhoneBoosterViewModel by viewModels()
    private var _binding: FragmentPhoneBoosterBinding? = null
    private val binding get() = _binding!!

    var mInterstitialAd: InterstitialAd? = null
    var act : MainActivity? = null
    var boolForAnim by Delegates.notNull<Boolean>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhoneBoosterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        act = activity as MainActivity

        //Get Ad Request
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        //Get memory
        val actManager = (activity as MainActivity).getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        actManager.getMemoryInfo(memInfo)
        val totalMemory = ceil((memInfo.totalMem / (1024 * 1024)).toDouble() / 1000).toInt()
        //val totalMemory = ceil((ActivityManager.MemoryInfo().totalMem / (1024 * 1024)).toDouble() / 1000).toInt()
        val usageMemory = (totalMemory * act!!.usageMemoryPercentGeneral.toDouble()).roundToInt() / 100.0

        viewModel.viewStates.observe(viewLifecycleOwner){ state ->
            when(state){
                is OptimizationStates.NotOptimize -> {
                    binding.apply {
                        configAd(adRequest, {
                            mInterstitialAd = it
                        }, { navigate() })
                        configGradients(0)
                        textRunningProcessesValue.text = "${act?.runningProcessGeneral}"
                        textRamUsageValue2.text = "${act?.usageMemoryPercentGeneral}%"
                        textRamUsageHelp.text = "$usageMemory GB / $totalMemory GB"
                        textProcess.text = "$usageMemory GB"
                        progressBar.progress = act?.usageMemoryPercentGeneral!!
                    }
                    if (act!!.optimizedPB) viewModel.endOptimization()
                    //Btn shake
                    else shakeAnim(boolForAnim, binding.btnOptimize)
                }
                is OptimizationStates.Optimization -> {
                    binding.apply {
                        btnOptimize.text = "Optimizing..."
                        (activity as MainActivity).viewPager?.isUserInputEnabled = false
                        val animation = ObjectAnimator.ofInt(progressBarCircle, "progress", 0, 100)
                        progressBarCircle.visibility = View.VISIBLE
                        animation.duration = 5 * 1000
                        animation.start()
                        textProcess.visibility = View.INVISIBLE
                        textProgressProc.visibility = View.VISIBLE
                        boolForAnim = true
                        imageOk.visibility=View.INVISIBLE
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
                }
                is OptimizationStates.Optimized -> {
                    val usageMemoryAfter  = (totalMemory * act?.usageMemoryPercentAfterGeneral!!.toDouble()).roundToInt() / 100.0
                    binding.apply {
                        configGradients(1)
                        act?.viewPager?.isUserInputEnabled = true
                        btnOptimize.text = "Optimized"
                        btnOptimize.isClickable = true
                        btnOptimize.setBackgroundDrawable(activity?.resources?.getDrawable(R.drawable.ic_gradient_blue))
                        textProcess.visibility = View.INVISIBLE
                        imageOk.visibility = View.VISIBLE
                        textRunningProcessesValue.text = "${act!!.runningProcessAfterGeneral}"
                        textRamUsageValue2.text = "${act?.usageMemoryPercentAfterGeneral}%"
                        textRamUsageHelp.text = "$usageMemoryAfter GB / $totalMemory GB"
                        imageView.setImageResource(R.drawable.ellipse_blue)
                        //todo change button back
                        textRamUsageValue2.setTextColor(ContextCompat.getColor(act!!, R.color.gradient_blue_start))
                        textRunningProcessesValue.setTextColor(ContextCompat.getColor(act!!, R.color.gradient_blue_start))
                        act?.optimizedPB = true
                        act?.optimizeSmth(Screen.PHONE_BOOSTER)
                        progressBar.progress = act?.usageMemoryPercentAfterGeneral!!
                        progressBarCircle.visibility = View.GONE
                        progressBar.progressDrawable = resources.getDrawable(R.drawable.progress_bar_hor_blue)
                        textProgressProc.visibility = View.INVISIBLE
                        btnOptimize.setTextColor(ContextCompat.getColor(act!!, R.color.white))
                        act?.onBottomBar()
                        act?.navigationView?.menu?.findItem(R.id.navigation_phone_booster)?.isEnabled = false
                    }
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
                setUpOrangeGradient(textRunningProcessesValue)
                setUpOrangeGradient(textRamUsageValue2)
                setUpOrangeGradient(textProcess)
            } else {
                setUpBlueGradient(textRunningProcessesValue)
                setUpBlueGradient(textRamUsageValue2)
                setUpBlueGradient(textProcess)
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
        (activity as MainActivity).navigationView?.menu?.findItem(R.id.navigation_phone_booster)?.isEnabled = true
    }

    override fun onResume() {
        super.onResume()
        act?.unblockAllExcept(Screen.PHONE_BOOSTER)
        //(activity as MainActivity).navigationView?.menu?.findItem(R.id.navigation_phone_booster)?.isEnabled = false
    }

    fun navigate(){
        findNavController().navigate(R.id.resultFragment,Bundle().apply { putString(FROM,"Phone Booster") })
    }
}