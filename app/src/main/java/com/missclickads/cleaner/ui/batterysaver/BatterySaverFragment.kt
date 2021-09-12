package com.missclickads.cleaner.ui.batterysaver

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context.BATTERY_SERVICE
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
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.missclickads.cleaner.App
import com.missclickads.cleaner.MainActivity
import com.missclickads.cleaner.R
import com.missclickads.cleaner.utils.Screen
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

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

    override fun onPause() {
        super.onPause()
        (activity as MainActivity).navigationView?.menu?.findItem(R.id.navigation_battery_saver)?.isEnabled = true
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var mInterstitialAd: InterstitialAd? = null
        var mAdView : AdView = view.findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        var boolForAnim = (activity as MainActivity).optimizedBS

        (activity as MainActivity).navigationView?.menu?.findItem(R.id.navigation_battery_saver)?.isEnabled = false
        val bm = (activity as MainActivity).getSystemService(BATTERY_SERVICE) as BatteryManager
        val batteryInfo = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        val textbattery = view.findViewById<TextView>(R.id.text_process)
        val textTime = view.findViewById<TextView>(R.id.text_time)
        val btnOptimize = view.findViewById<Button>(R.id.btn_optimize)
        val imageOk = view.findViewById<ImageView>(R.id.image_ok)
        val imageCircle = view.findViewById<ImageView>(R.id.imageView)
        val textResult = view.findViewById<TextView>(R.id.text_process)
        val progressBarCircle = view.findViewById<ProgressBar>(R.id.progressBarCircle)
        val progressProc = view.findViewById<TextView>(R.id.text_progressProc)
        textbattery.text ="$batteryInfo %"

        //before optimize
        val batteryhours = batteryInfo * 4 /60
        val batteryminutes = batteryInfo *4 % 60

        textTime.text="$batteryhours h $batteryminutes m"
        val paint = textResult.paint
        val width = paint.measureText(textResult.text.toString())
        val textShader: Shader = LinearGradient(0f, 0f, width, textResult.textSize, intArrayOf(
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_start) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_middle) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_end)

        ), null, Shader.TileMode.CLAMP)
        textResult.paint.setShader(textShader)

        val paint2 = textTime.paint
        val width2 = paint2.measureText(textTime.text.toString())
        val textShader2: Shader = LinearGradient(0f, 0f, width2, textTime.textSize, intArrayOf(
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_start) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_middle) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_end)
        ), null, Shader.TileMode.CLAMP)
        textTime.paint.setShader(textShader2)


        //after optimize
        val batteryhoursafter = (batteryInfo * 1.2 * 4 / 60).toInt()
        val batteryminutesafter = (batteryInfo * 1.2 * 4 % 60).toInt()
        fun optimized(){
            btnOptimize.text = "Optimized"
            btnOptimize.setBackgroundDrawable(activity?.resources?.getDrawable(R.drawable.ic_gradient_blue))

            (activity as MainActivity).onBottomBar()
            (activity as MainActivity).navigationView?.menu?.findItem(R.id.navigation_battery_saver)?.isEnabled = false



            textResult.visibility = View.INVISIBLE
            imageOk.visibility = View.VISIBLE
            imageCircle.setImageResource(R.drawable.ellipse_blue)
            textTime.setTextColor(ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start))
            (activity as MainActivity).optimizedBS = true
            (activity as MainActivity).optimizeSmth(Screen.BATTERY_SAVER)
            progressBarCircle.visibility = View.GONE
            progressProc.visibility = View.INVISIBLE
            btnOptimize.setTextColor(ContextCompat.getColor((activity as MainActivity), R.color.white))

            val paint = textResult.paint
            val width = paint.measureText(textResult.text.toString())
            val textShader: Shader = LinearGradient(0f, 0f, width, textResult.textSize, intArrayOf(
                ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_end) ,
                ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_middle) ,
                ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start)
            ), null, Shader.TileMode.CLAMP)
            textResult.paint.setShader(textShader)

            val paint2 = textTime.paint
            val width2 = paint2.measureText(textTime.text.toString())
            val textShader2: Shader = LinearGradient(0f, 0f, width2, textTime.textSize, intArrayOf(
                ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_end) ,
                ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_middle) ,
                ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start)
            ), null, Shader.TileMode.CLAMP)
            textTime.paint.setShader(textShader2)
            textTime.text = "$batteryhoursafter h $batteryminutesafter m"


        }
        if ((activity as MainActivity).optimizedBS) optimized()
        else {
            Handler().postDelayed({
            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(Runnable {

                    try {
                        val animation = AnimationUtils.loadAnimation((activity as MainActivity), R.anim.shake)
                        if (!boolForAnim) {
                            btnOptimize.startAnimation(animation)
                        }


                    }
                    catch (e: Exception)
                    {
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
        btnOptimize.setOnClickListener {
            if(!(activity as MainActivity).optimizedBS ){
                btnOptimize.text = "Optimizing..."
                textResult.visibility = View.INVISIBLE
                progressProc.visibility = View.VISIBLE
                boolForAnim = true
                (activity as MainActivity).offBottomBar()

                btnOptimize.isClickable = false
                val animation = ObjectAnimator.ofInt(progressBarCircle, "progress", 0, 100)
                progressBarCircle.visibility = View.VISIBLE
                animation.duration = 5 * 1000
                animation.start()
                btnOptimize.setTextColor(ContextCompat.getColor((activity as MainActivity), R.color.gray))
                btnOptimize.setBackgroundDrawable(activity?.resources?.getDrawable(R.drawable.ic_gradient_blue_dark))
                Handler().postDelayed({ optimized()
                                            showAd()}, (5 * 1000).toLong())


                imageCircle.setImageResource(R.drawable.ellipse_blue)
                val paint = progressProc.paint
                val width = paint.measureText(progressProc.text.toString())
                val textShader: Shader = LinearGradient(0f, 0f, width, progressProc.textSize, intArrayOf(
                    ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_end) ,
                    ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_middle) ,
                    ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start)
                ), null, Shader.TileMode.CLAMP)
                progressProc.paint.setShader(textShader)



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

        }




        //ads
        InterstitialAd.load(
            activity as MainActivity,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError?.message)
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded")
                    mInterstitialAd = interstitialAd
                    mInterstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                Log.d(TAG, "Ad was dismissed.")

                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                                Log.d(TAG, "Ad failed to show.")
                            }


                            override fun onAdShowedFullScreenContent() {
                                Log.d(TAG, "Ad showed fullscreen content.")
                                mInterstitialAd = null
                            }
                        }
                }
            })

    }
}



