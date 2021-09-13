package com.missclickads.cleaner

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class SplashActivity : AppCompatActivity() {

    private var mInterstitialAd: InterstitialAd? = null
    private var TAG = "Splash"

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        MobileAds.initialize(this) {}

        setContentView(R.layout.activity_splash)
        val progressBar = findViewById<ProgressBar>(R.id.progressBarSplash)
        val animation = ObjectAnimator.ofInt(progressBar, "progress", 0, 100)
        animation.duration = (8.5 * 1000).toLong()
        animation.start()
        Handler().postDelayed({
            if (mInterstitialAd != null && App.isActivityVisible) {
                mInterstitialAd?.show(this@SplashActivity)
                println("Ads go!")
            } else {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, (8.5 * 1000).toLong())


        //ads
        var adRequest = AdRequest.Builder().build()



        InterstitialAd.load(
            this,
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
                                mInterstitialAd = null
                                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                                startActivity(intent)
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


    override fun onResume() {
        super.onResume()
        App.activityResumed()
    }

    override fun onPause() {
        super.onPause()
        App.activityPaused()
    }




}