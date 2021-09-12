package com.missclickads.cleaner.ui.optimizer

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
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


class OptimizerFragment : Fragment() {

    private lateinit var optimizerViewModel: OptimizerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        optimizerViewModel =
            ViewModelProvider(this).get(OptimizerViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_optimizer, container, false)

        return root
    }
    override fun onPause() {
        super.onPause()
        (activity as MainActivity).navigationView?.menu?.findItem(R.id.navigation_optimizer)?.isEnabled = true
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).navigationView?.menu?.findItem(R.id.navigation_optimizer)?.isEnabled = false
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textResult = view.findViewById<TextView>(R.id.text_process)
        val imageCircle = view.findViewById<ImageView>(R.id.imageView)
        val btnOptimize = view.findViewById<Button>(R.id.btn_optimize)
        val progressBarCircle = view.findViewById<ProgressBar>(R.id.progressBarCircle)
        val progressProc = view.findViewById<TextView>(R.id.text_progressProc)
        val textApp1 = view.findViewById<TextView>(R.id.text_app1)
        val textApp2 = view.findViewById<TextView>(R.id.text_app2)
        val textApp3 = view.findViewById<TextView>(R.id.text_app3)
        val textApp4 = view.findViewById<TextView>(R.id.text_app4)
        val textApp5 = view.findViewById<TextView>(R.id.text_app5)
        val textAppRandom1 = (activity as MainActivity).textAppRandom1General
        val textAppRandom2 = (activity as MainActivity).textAppRandom2General
        val textAppRandom3 = (activity as MainActivity).textAppRandom3General
        val textAppRandom4 = (activity as MainActivity).textAppRandom4General
        val textAppRandom5 = (activity as MainActivity).textAppRandom5General
        val textAppAfterR1 = textAppRandom1 / 10.0
        val textAppAfterR2 = textAppRandom2 / 10.0
        val textAppAfterR3 = textAppRandom3 / 10.0
        val textAppAfterR4 = textAppRandom4 / 10.0
        val textAppAfterR5 = textAppRandom5 / 10.0

        textApp1.text = "$textAppAfterR1 MB"
        textApp2.text = "$textAppAfterR2 MB"
        textApp3.text = "$textAppAfterR3 MB"
        textApp4.text = "$textAppAfterR4 MB"
        textApp5.text = "$textAppAfterR5 MB"

        val textAppRandomAfter1 = (activity as MainActivity).textAppRandomAfter1General
        val textAppRandomAfter2 = (activity as MainActivity).textAppRandomAfter2General
        val textAppRandomAfter3 = (activity as MainActivity).textAppRandomAfter3General
        val textAppRandomAfter4 = (activity as MainActivity).textAppRandomAfter4General
        val textAppRandomAfter5 = (activity as MainActivity).textAppRandomAfter5General
        val textAppAfterOtp1 = textAppRandomAfter1 / 10.0
        val textAppAfterOtp2 = textAppRandomAfter2 / 10.0
        val textAppAfterOtp3 = textAppRandomAfter3 / 10.0
        val textAppAfterOtp4 = textAppRandomAfter4 / 10.0
        val textAppAfterOtp5 = textAppRandomAfter5 / 10.0







        val imageApps = listOf(
            view.findViewById<ImageView>(R.id.imageApp1),
            view.findViewById<ImageView>(R.id.imageApp2),
            view.findViewById<ImageView>(R.id.imageApp3),
            view.findViewById<ImageView>(R.id.imageApp4),
            view.findViewById<ImageView>(R.id.imageApp5),
        )

        var mInterstitialAd: InterstitialAd? = null
        var mAdView : AdView = view.findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        var boolForAnim = (activity as MainActivity).optimizedOpt

        //before optimize
        val temp = (40..50).random()
        //after optimize
        val tempAfter = (temp * 0.85).toInt()



        //telephone's apps info
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
            lateinit var appIcon:Drawable
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





        val paint = textResult.paint
        val width = paint.measureText(textResult.text.toString())
        val textShader: Shader = LinearGradient(
            0f, 0f, width, textResult.textSize, intArrayOf(
                ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_start),
                ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_middle),
                ContextCompat.getColor((activity as MainActivity), R.color.gradient_orange_end)

            ), null, Shader.TileMode.CLAMP
        )
        textResult.paint.setShader(textShader)

        fun showAd(){
            if (mInterstitialAd != null && App.isActivityVisible) {
                mInterstitialAd?.show(activity as MainActivity)
                println("Ads go!")
            }
        }

        fun optimized() {
            btnOptimize.text = "Optimized"
            (activity as MainActivity).viewPager?.isUserInputEnabled = true
            textApp1.text = "$textAppAfterOtp1 MB"
            textApp2.text = "$textAppAfterOtp2 MB"
            textApp3.text = "$textAppAfterOtp3 MB"
            textApp4.text = "$textAppAfterOtp4 MB"
            textApp5.text = "$textAppAfterOtp5 MB"


            btnOptimize.setBackgroundDrawable(activity?.resources?.getDrawable(R.drawable.ic_gradient_blue))
            textResult.text = "$tempAfter°C"
            imageCircle.setImageResource(R.drawable.ellipse_blue)
            textResult.setTextColor(
                ContextCompat.getColor(
                    (activity as MainActivity),
                    R.color.gradient_blue_start
                )
            )
            imageCircle.setImageResource(R.drawable.ellipse_blue)
            (activity as MainActivity).optimizedOpt = true
            (activity as MainActivity).optimizeSmth(Screen.OPTIMIZER)
            progressBarCircle.visibility = View.GONE
            progressProc.visibility = View.INVISIBLE
            textResult.visibility = View.VISIBLE
            btnOptimize.setTextColor(
                ContextCompat.getColor(
                    (activity as MainActivity),
                    R.color.white
                )
            )
            val paint = textResult.paint
            val width = paint.measureText(textResult.text.toString())
            val textShader: Shader = LinearGradient(
                0f, 0f, width, textResult.textSize, intArrayOf(
                    ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_end),
                    ContextCompat.getColor(
                        (activity as MainActivity),
                        R.color.gradient_blue_middle
                    ),
                    ContextCompat.getColor((activity as MainActivity), R.color.gradient_blue_start)

                ), null, Shader.TileMode.CLAMP
            )
            textResult.paint.setShader(textShader)


            (activity as MainActivity).onBottomBar()
            (activity as MainActivity).navigationView?.menu?.findItem(R.id.navigation_optimizer)?.isEnabled = false
        }
        if ((activity as MainActivity).optimizedOpt) optimized()
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
        btnOptimize.setOnClickListener {
            if (!(activity as MainActivity).optimizedOpt) {
                (activity as MainActivity).viewPager?.isUserInputEnabled = false
                btnOptimize.text = "Optimizing..."
                textResult.visibility = View.INVISIBLE
                progressProc.visibility = View.VISIBLE
                btnOptimize.isClickable = false
                boolForAnim = true

                textApp1.text = "..."
                textApp2.text = "..."
                textApp3.text = "..."
                textApp4.text = "..."
                textApp5.text = "..."

                (activity as MainActivity).offBottomBar()

                imageCircle.setImageResource(R.drawable.ellipse_blue)
                val animation = ObjectAnimator.ofInt(progressBarCircle, "progress", 0, 100)
                progressBarCircle.visibility = View.VISIBLE
                animation.duration = 5 * 1000
                animation.start()
                btnOptimize.setTextColor(
                    ContextCompat.getColor(
                        (activity as MainActivity),
                        R.color.gray
                    )
                )
                btnOptimize.setBackgroundDrawable(activity?.resources?.getDrawable(R.drawable.ic_gradient_blue_dark))
                Handler().postDelayed({
                    optimized()
                    showAd()
                }, (5 * 1000).toLong())
                val paint = progressProc.paint
                val width = paint.measureText(progressProc.text.toString())
                val textShader: Shader = LinearGradient(
                    0f, 0f, width, progressProc.textSize, intArrayOf(
                        ContextCompat.getColor(
                            (activity as MainActivity),
                            R.color.gradient_blue_end
                        ),
                        ContextCompat.getColor(
                            (activity as MainActivity),
                            R.color.gradient_blue_middle
                        ),
                        ContextCompat.getColor(
                            (activity as MainActivity),
                            R.color.gradient_blue_start
                        )

                    ), null, Shader.TileMode.CLAMP
                )
                progressProc.paint.setShader(textShader)


                for( i in 0..99){

                    Handler().postDelayed({
                        if (i == 50) {
                            val paint = progressProc.paint
                            val width = paint.measureText(progressProc.text.toString())
                            val textShader: Shader = LinearGradient(
                                0f, 0f, width, progressProc.textSize, intArrayOf(
                                    ContextCompat.getColor(
                                        (activity as MainActivity),
                                        R.color.gradient_blue_end
                                    ),
                                    ContextCompat.getColor(
                                        (activity as MainActivity),
                                        R.color.gradient_blue_middle
                                    ),
                                    ContextCompat.getColor(
                                        (activity as MainActivity),
                                        R.color.gradient_blue_start
                                    )

                                ), null, Shader.TileMode.CLAMP
                            )
                            progressProc.paint.setShader(textShader)
                        }
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

