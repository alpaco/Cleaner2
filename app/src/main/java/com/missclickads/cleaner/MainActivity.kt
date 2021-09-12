package com.missclickads.cleaner

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.findFragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.MobileAds
import com.missclickads.cleaner.adapters.ViewPagerAdapter
import com.missclickads.cleaner.utils.DATA_PATTERN
import com.missclickads.cleaner.utils.OptimizeData
import com.missclickads.cleaner.utils.OptimizeDataRepository
import com.missclickads.cleaner.utils.Screen
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var data: OptimizeDataRepository
    var optimizedPB = false
    var optimizedJC = false
    var optimizedOpt = false
    var optimizedBS = false
    var navigationView: BottomNavigationView? = null
    var viewPager : ViewPager2? = null

    //var block = false

    //random values for Phone Booster
    val usageMemoryPercentGeneral = (60..95).random()
    val runningProcessGeneral = (1150..1483).random()
    val usageMemoryPercentAfterGeneral = (25..43).random()
    val runningProcessAfterGeneral  = (240..470).random()
    //random junk cleaner
    val usageMemoryGeneral = (150..500).random()
    //random Optimizer
    val textAppRandom1General = (100..300).random().toLong()
    val textAppRandom2General = (100..300).random().toLong()
    val textAppRandom3General = (100..300).random().toLong()
    val textAppRandom4General = (100..300).random().toLong()
    val textAppRandom5General = (100..300).random().toLong()
    val textAppRandomAfter1General = (10..50).random().toLong()
    val textAppRandomAfter2General = (10..50).random().toLong()
    val textAppRandomAfter3General = (10..50).random().toLong()
    val textAppRandomAfter4General = (10..50).random().toLong()
    val textAppRandomAfter5General = (10..50).random().toLong()

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //ads
        MobileAds.initialize(this) {}


        data = OptimizeDataRepository(this)
        val states = data.getData()
        val currentData = Date()
        optimizedBS = currentData.time - states.batterySaver.time <= 60 * 60 * 2 * 1000
        optimizedPB = currentData.time - states.phoneBooster.time <= 60 * 60 * 2 * 1000
        optimizedJC = currentData.time - states.junkCleaner.time <= 60 * 60 * 2 * 1000
        optimizedOpt = currentData.time - states.optimizer.time <= 60 * 60 * 2 * 1000

        println(optimizedPB)
        println(optimizedJC)
        println(optimizedOpt)
        println(optimizedBS)

        setContentView(R.layout.activity_main)
        navigationView = findViewById(R.id.nav_view)
        viewPager = findViewById<ViewPager2>(R.id.viewPager2)
//        viewPager!!.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageScrollStateChanged(state: Int) {
//                super.onPageScrollStateChanged(state)
//                viewPager!!.isUserInputEnabled = !block
//            }
//        })
        navigationView?.menu?.findItem(R.id.navigation_phone_booster)?.isEnabled = false
        val viewPagerAdapter = ViewPagerAdapter(this)
        viewPager!!.adapter = viewPagerAdapter
        navigationView!!.setOnNavigationItemSelectedListener  {
            //Log.e("NavSetSelected", it.toString())
            when(it.itemId){
                R.id.navigation_battery_saver -> viewPager!!.currentItem = 1
                R.id.navigation_phone_booster -> viewPager!!.currentItem = 0
                R.id.navigation_junk_cleaner -> viewPager!!.currentItem = 3
                R.id.navigation_optimizer -> viewPager!!.currentItem = 2
            }
            false
        }



        //navigationView?.menu?.findItem(R.id.navigation_junk_cleaner)?.setIcon(R.drawable.ic_tabjc)
        supportActionBar?.hide()
       // window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        navigationView?.itemIconTintList = null
        //val navController = findNavController(R.id.nav_host_fragment)

//        navController.addOnDestinationChangedListener { controller, destination, arguments ->
//            when(destination.id){
//                R.id.navigation_battery_saver -> viewPager.currentItem = 1
//                R.id.navigation_phone_booster -> viewPager.currentItem = 0
//                R.id.navigation_junk_cleaner -> viewPager.currentItem = 3
//                R.id.navigation_optimizer -> viewPager.currentItem = 2
//            }
//            //viewPager.currentItem =
//        }

        viewPager!!.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0 -> {
                        navigationView?.menu?.findItem(R.id.navigation_phone_booster)?.isChecked = true
                    }//navController.navigate(R.id.navigation_phone_booster)
                    1 -> {
                        //navController.navigate(R.id.navigation_battery_saver)
                        navigationView?.menu?.findItem(R.id.navigation_battery_saver)?.isChecked = true
                    }
                    2 -> {
                       // navController.navigate(R.id.navigation_optimizer)
                        navigationView?.menu?.findItem(R.id.navigation_optimizer)?.isChecked = true
                    }
                    3 -> {
                        //navController.navigate(R.id.navigation_junk_cleaner)
                        navigationView?.menu?.findItem(R.id.navigation_junk_cleaner)?.isChecked = true
                    }
                }
            }
        })
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_phone_booster, R.id.navigation_battery_saver, R.id.navigation_optimizer, R.id.navigation_junk_cleaner))

       // setupActionBarWithNavController(navController, appBarConfiguration)
        //navigationView?.setupWithNavController(navController)
        if(!optimizedPB) navigationView?.menu?.findItem(R.id.navigation_phone_booster)?.icon = resources.getDrawable(R.drawable.ic_tab_booster_fire_new)
        //todo uncommit it after fix xml
        if(!optimizedBS) navigationView?.menu?.findItem(R.id.navigation_battery_saver)?.icon = resources.getDrawable(R.drawable.ic_tab_battery_fire_new)
        if(!optimizedOpt) navigationView?.menu?.findItem(R.id.navigation_optimizer)?.icon = resources.getDrawable(R.drawable.ic_tab_optimize_fire_new)
        if(!optimizedJC) navigationView?.menu?.findItem(R.id.navigation_junk_cleaner)?.icon = resources.getDrawable(R.drawable.ic_tab_delete_fire_new)
    }

    fun optimizeSmth(type: Screen){
        if(type == Screen.PHONE_BOOSTER) navigationView?.menu?.findItem(R.id.navigation_phone_booster)?.setIcon(R.drawable.ic_tab_booster_new)
        if(type == Screen.BATTERY_SAVER) navigationView?.menu?.findItem(R.id.navigation_battery_saver)?.setIcon(R.drawable.ic_tab_battery_new)
        if(type == Screen.OPTIMIZER) navigationView?.menu?.findItem(R.id.navigation_optimizer)?.setIcon(R.drawable.ic_tab_optimize_new)
        if(type == Screen.JUNK_CLEANER) navigationView?.menu?.findItem(R.id.navigation_junk_cleaner)?.setIcon(R.drawable.ic_tab_delete_new)
        data.putData( Date(),type)
    }

    fun offBottomBar(){
        navigationView?.menu?.findItem(R.id.navigation_phone_booster)?.isEnabled = false
        navigationView?.menu?.findItem(R.id.navigation_optimizer)?.isEnabled = false
        navigationView?.menu?.findItem(R.id.navigation_junk_cleaner)?.isEnabled = false
        navigationView?.menu?.findItem(R.id.navigation_battery_saver)?.isEnabled = false
    }

    fun onBottomBar(){
        navigationView?.menu?.findItem(R.id.navigation_phone_booster)?.isEnabled = true
        navigationView?.menu?.findItem(R.id.navigation_optimizer)?.isEnabled = true
        navigationView?.menu?.findItem(R.id.navigation_junk_cleaner)?.isEnabled = true
        navigationView?.menu?.findItem(R.id.navigation_battery_saver)?.isEnabled = true
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

