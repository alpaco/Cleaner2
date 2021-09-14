package com.missclickads.cleaner.base

import android.content.ContentValues
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Handler
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.missclickads.cleaner.MainActivity
import com.missclickads.cleaner.R
import java.lang.Exception
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

open class BaseFragment() : Fragment() {
    fun shakeAnim(boolForAnim : Boolean, btnOptimize : Button){
        Handler().postDelayed({
            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate({
                try {
                    val animation = AnimationUtils.loadAnimation((activity as MainActivity), R.anim.shake)
                    if (!boolForAnim) {
                        btnOptimize.startAnimation(animation)
                    }
                } catch (e: Exception) {
                    println(e)
                }

            }, 0, 5, TimeUnit.SECONDS)
        }, (1500).toLong())
    }

    fun progressText(text : TextView){
        val progressProc = text
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

    fun setUpOrangeGradient(tvText : TextView){
        val paint = tvText.paint
        val width = paint.measureText(tvText.text.toString())
        val textShader2: Shader = LinearGradient(0f, 0f, width, tvText.textSize, intArrayOf(
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_start) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_middle) ,
            ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_end)
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

    fun configAd(adRequest : AdRequest,
                 callback: (InterstitialAd?) -> (Unit),
                 navigate: () -> (Unit)
    ){

        InterstitialAd.load(
            activity as MainActivity,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(ContentValues.TAG, adError?.message)
                    //mInterstitialAd = null
                    callback(null)
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(ContentValues.TAG, "Ad was loaded")
                    //mInterstitialAd = interstitialAd
                    interstitialAd.fullScreenContentCallback =
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
                                //mInterstitialAd = null
                                callback(null)
                            }
                        }
                    callback(interstitialAd)
                }
            })
    }
}