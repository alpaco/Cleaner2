package com.missclickads.cleaner.ui.optimizer

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
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.missclickads.cleaner.App
import com.missclickads.cleaner.MainActivity
import com.missclickads.cleaner.R
import com.missclickads.cleaner.databinding.FragmentOptimizerBinding
import com.missclickads.cleaner.databinding.FragmentPhoneBoosterBinding
import com.missclickads.cleaner.states.OptimizationStates
import com.missclickads.cleaner.ui.phonebooster.PhoneBoosterViewModel
import com.missclickads.cleaner.utils.Screen
import java.lang.Exception
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class OptimizerFragmentRefactor : Fragment(R.layout.fragment_optimizer) {

    private val viewModel : OptimizerViewModel by viewModels()
    private var _binding: FragmentOptimizerBinding? = null
    private val binding get() = _binding!!

    var mInterstitialAd: InterstitialAd? = null
    var act : MainActivity? = null
    var boolForAnim by Delegates.notNull<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOptimizerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        act = activity as MainActivity
        boolForAnim = act!!.optimizedOpt

        //Get Ad Request
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)


        viewModel.viewStates.observe(viewLifecycleOwner) { state ->
            when(state){
                is OptimizationStates.NotOptimize -> {
                    //todo before optimize
                    binding.apply {
                        configAd(adRequest)
                        configGradients(0)
                        textApp1.text= "${act!!.textAppRandom1General / 10.0} MB"
                        Log.e("asadsadad","SAFDsadf")
                        textApp2.text = "${act!!.textAppRandom2General / 10.0} MB"
                        textApp3.text = "${act!!.textAppRandom3General / 10.0} MB"
                        textApp4.text = "${act!!.textAppRandom4General / 10.0} MB"
                        textApp5.text = "${act!!.textAppRandom5General / 10.0} MB"
                        textProcess.text = "${act?.textResultGeneral}°C"
                        appInfo()

                    }
                    if (act!!.optimizedOpt) viewModel.endOptimization()
                    //Btn shake
                    else shakeAnim()


                }
                is OptimizationStates.Optimization -> {
                    //todo optimization in process

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
                        (activity as MainActivity).offBottomBar()
                        textApp1.text = "..."
                        textApp2.text = "..."
                        textApp3.text = "..."
                        textApp4.text = "..."
                        textApp5.text = "..."
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





                }
                is OptimizationStates.Optimized -> {
                    binding.apply {
                        configGradients(1)
                        act?.viewPager?.isUserInputEnabled = true
                        btnOptimize.text = "Optimized"
                        btnOptimize.isClickable = true
                        btnOptimize.setBackgroundDrawable(activity?.resources?.getDrawable(R.drawable.ic_gradient_blue))
                        textProcess.visibility = View.VISIBLE
                        textApp1.text= "${act!!.textAppRandomAfter1General / 10.0} MB"
                        textApp2.text = "${act!!.textAppRandomAfter2General / 10.0} MB"
                        textApp3.text = "${act!!.textAppRandomAfter3General / 10.0} MB"
                        textApp4.text = "${act!!.textAppRandomAfter4General / 10.0} MB"
                        textApp5.text = "${act!!.textAppRandomAfter5General / 10.0} MB"
                        textProcess.text = "${(act!!.textResultGeneral * 0.85).toInt()}°C"
                        imageView.setImageResource(R.drawable.ellipse_blue)
                        //todo change button back
                        act?.optimizedOpt = true
                        act?.optimizeSmth(Screen.OPTIMIZER)
                        progressBarCircle.visibility = View.GONE
                        textProgressProc.visibility = View.INVISIBLE
                        btnOptimize.setTextColor(ContextCompat.getColor(act!!, R.color.white))
                        act?.onBottomBar()
                        act?.navigationView?.menu?.findItem(R.id.navigation_optimizer)?.isEnabled = false
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
            } else {

                setUpBlueGradient(textProcess)
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
        (activity as MainActivity).navigationView?.menu?.findItem(R.id.navigation_optimizer)?.isEnabled = true
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).navigationView?.menu?.findItem(R.id.navigation_optimizer)?.isEnabled = false
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
    fun appInfo() {

        val imageApps = listOf(
           binding.imageApp1,
            binding.imageApp2,
            binding.imageApp3,
            binding.imageApp4,
            binding.imageApp5

        )

        val pm: PackageManager = (activity as MainActivity).packageManager
        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        var image = 0
        for (i in packages.indices) {
            val ai = pm.getApplicationInfo(packages[i].packageName, 0)
            if (ai.flags and ApplicationInfo.FLAG_SYSTEM != 0) {
                continue
            }
            val res: Resources = pm.getResourcesForApplication(packages[i])
            val config: Configuration = res.getConfiguration()
            val originalConfig = Configuration(config)
            config.densityDpi = DisplayMetrics.DENSITY_XHIGH
            val dm: DisplayMetrics = res.getDisplayMetrics()
            res.updateConfiguration(config, dm)
            lateinit var appIcon: Drawable
            try {
                appIcon = res.getDrawable(packages[i].icon)
            }
            catch (e: java.lang.Exception){
                continue
            }
            res.updateConfiguration(originalConfig, dm)
            imageApps[image].setImageDrawable(appIcon)
            image +=1
            if (image == 5) break
        }
    }
}