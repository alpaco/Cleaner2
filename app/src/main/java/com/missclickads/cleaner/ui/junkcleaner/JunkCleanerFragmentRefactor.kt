package com.missclickads.cleaner.ui.junkcleaner

import android.animation.ObjectAnimator
import android.content.ContentValues
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
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
import com.missclickads.cleaner.databinding.FragmentJunkCleanerBinding
import com.missclickads.cleaner.databinding.FragmentOptimizerBinding
import com.missclickads.cleaner.states.OptimizationStates
import com.missclickads.cleaner.ui.optimizer.OptimizerViewModel
import com.missclickads.cleaner.ui.result.FROM
import com.missclickads.cleaner.utils.Screen
import java.lang.Exception
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class JunkCleanerFragmentRefactor : Fragment(R.layout.fragment_junk_cleaner) {


    private val viewModel : JunkCleanerViewModel by viewModels()
    private var _binding: FragmentJunkCleanerBinding? = null
    private val binding get() = _binding!!

    var mInterstitialAd: InterstitialAd? = null
    var act : MainActivity? = null
    var boolForAnim by Delegates.notNull<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJunkCleanerBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        act = activity as MainActivity
        boolForAnim = act!!.optimizedJC
        act!!.unblockAllExcept(Screen.JUNK_CLEANER)
        //Get Ad Request
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)





        viewModel.viewStates.observe(viewLifecycleOwner){ state ->
            when(state){
                is OptimizationStates.NotOptimize -> {

                    binding.apply {
                        configAd(adRequest)
                        configGradients(0)
                        textProcess.text= "${act?.usageMemoryGeneral} MB"

                        Log.e("asadsadad","SAFDsadf")
                    }
                    if (act!!.optimizedJC) viewModel.endOptimization()
                    //Btn shake
                    else shakeAnim()
                    //todo before optimize
                }
                is OptimizationStates.Optimization -> {

                    binding.apply {

                        btnOptimize.text = "Optimizing..."
                        textCleaningRequired.text = "cleaning..."
                        textCleaningRequired2.text = "cleaning..."
                        textCleaningRequired3.text = "cleaning..."
                        textCleaningRequired4.text = "cleaning..."
                        configGradients(0)
                        (activity as MainActivity).viewPager?.isUserInputEnabled = false
                        val animation = ObjectAnimator.ofInt(progressBarCircle, "progress", 0, 100)
                        progressBarCircle.visibility = View.VISIBLE
                        animation.duration = 5 * 1000
                        animation.start()
                        imageOk.visibility = View.INVISIBLE
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
                    progressText()
                    //todo optimization in process
                }
                is OptimizationStates.Optimized -> {
                    binding.apply {
                        configGradients(1)
                        act?.viewPager?.isUserInputEnabled = true
                        btnOptimize.text = "Optimized"
                        textCleaningRequired.text = "cleared"
                        textCleaningRequired2.text = "cleared"
                        textCleaningRequired3.text = "cleared"
                        textCleaningRequired4.text = "cleared"
                        btnOptimize.isClickable = true
                        btnOptimize.setBackgroundDrawable(activity?.resources?.getDrawable(R.drawable.ic_gradient_blue))
                        textProcess.visibility = View.INVISIBLE
                        imageOk.visibility = View.VISIBLE
                        imageView.setImageResource(R.drawable.ellipse_blue)
                        //todo change button back
                        act?.optimizedJC = true
                        act?.optimizeSmth(Screen.JUNK_CLEANER)
                        progressBarCircle.visibility = View.GONE
                        textProgressProc.visibility = View.INVISIBLE
                        btnOptimize.setTextColor(ContextCompat.getColor(act!!, R.color.white))
                        act?.onBottomBar()
                        act?.navigationView?.menu?.findItem(R.id.navigation_phone_booster)?.isEnabled = false


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
                setUpOrangeGradient(textCleaningRequired)
                setUpOrangeGradient(textCleaningRequired2)
                setUpOrangeGradient(textCleaningRequired3)
                setUpOrangeGradient(textCleaningRequired4)

            } else {

                setUpBlueGradient(textProcess)
                setUpBlueGradient(textCleaningRequired)
                setUpBlueGradient(textCleaningRequired2)
                setUpBlueGradient(textCleaningRequired3)
                setUpBlueGradient(textCleaningRequired4)

            }

        }
    }

    fun setUpOrangeGradient(tvText : TextView){
        val paint = tvText.paint
        val width = paint.measureText(tvText.text.toString())
        val textShader2: Shader = LinearGradient(0f, 0f, width, tvText.textSize, intArrayOf(
            ContextCompat.getColor(act!!, R.color.gradient_orange_start) ,
            ContextCompat.getColor(act!!, R.color.gradient_orange_middle) ,
            ContextCompat.getColor(act!!, R.color.gradient_orange_end)
        ), null, Shader.TileMode.CLAMP)
        tvText.paint.setShader(textShader2)
    }

    fun setUpBlueGradient(tvText: TextView){
        val paint = tvText.paint
        val width = paint.measureText(tvText.text.toString())
        val textShader2: Shader = LinearGradient(0f, 0f, width, tvText.textSize, intArrayOf(
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_end) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_middle) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start)
        ), null, Shader.TileMode.CLAMP)
        tvText.paint.setShader(textShader2)
    }

    fun shakeAnim(){
        Handler().postDelayed({
            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate({
                try {
                    val animation = AnimationUtils.loadAnimation(act!!, R.anim.shake)
                    if (!boolForAnim) {
                        binding.btnOptimize.startAnimation(animation)
                    }
                } catch (e: Exception) {
                    println(e)
                }

            }, 0, 5, TimeUnit.SECONDS)
        }, (1500).toLong())
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

    fun progressText(){
        val progressProc = binding.textProgressProc
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
                    progressProc.paint.setShader(textShader) }
                progressProc.text = "$i %"
            }, (i * 50).toLong())
        }
    }

    override fun onPause() {
        super.onPause()
        //(activity as MainActivity).navigationView?.menu?.findItem(R.id.navigation_junk_cleaner)?.isEnabled = true
    }

    override fun onResume() {
        super.onResume()
        act!!.unblockAllExcept(Screen.JUNK_CLEANER)
    }

    fun configAd(adRequest : AdRequest){

        InterstitialAd.load(
            activity as MainActivity,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(ContentValues.TAG, adError?.message)
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(ContentValues.TAG, "Ad was loaded")
                    mInterstitialAd = interstitialAd
                    mInterstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                Log.d(ContentValues.TAG, "Ad was dismissed.")
                                navigate()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                                Log.d(ContentValues.TAG, "Ad failed to show.")
                            }


                            override fun onAdShowedFullScreenContent() {
                                Log.d(ContentValues.TAG, "Ad showed fullscreen content.")
                                mInterstitialAd = null
                            }
                        }
                }
            })
    }

    fun navigate(){
        findNavController().navigate(R.id.resultFragment,Bundle().apply { putString(FROM,"Junk Cleaner") })
    }

}