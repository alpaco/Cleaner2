package com.missclickads.cleaner

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.Window
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
import com.missclickads.cleaner.dialog.ExitDialogFragment
import com.missclickads.cleaner.utils.DATA_PATTERN
import com.missclickads.cleaner.utils.OptimizeData
import com.missclickads.cleaner.utils.OptimizeDataRepository
import com.missclickads.cleaner.utils.Screen
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    lateinit var data: OptimizeDataRepository
    var optimizedPB = false
    var optimizedJC = false
    var optimizedOpt = false
    var optimizedBS = false
    var navigationView: BottomNavigationView? = null
    var viewPager : ViewPager2? = null
    var block = false

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
    val textResultGeneral = (40..50).random()

    fun callbackFromDialog(id : Int){
       viewPager?.currentItem = id
    }

    fun setupViewPagerFromFragment(viewPager2: ViewPager2){
        viewPager = viewPager2
        setUpBottomNavWithViewPager()
    }

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

        //navigationView?.menu?.findItem(R.id.navigation_junk_cleaner)?.setIcon(R.drawable.ic_tabjc)
        supportActionBar?.hide()
       // window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        navigationView?.itemIconTintList = null
        //val navController = findNavController(R.id.nav_host_fragment)

        //navigationView?.setupWithNavController(navController)
        if(!optimizedPB) navigationView?.menu?.findItem(R.id.navigation_phone_booster)?.icon = resources.getDrawable(R.drawable.ic_tab_booster_fire_new)
        if(!optimizedBS) navigationView?.menu?.findItem(R.id.navigation_battery_saver)?.icon = resources.getDrawable(R.drawable.ic_tab_battery_fire_new)
        if(!optimizedOpt) navigationView?.menu?.findItem(R.id.navigation_optimizer)?.icon = resources.getDrawable(R.drawable.ic_tab_optimize_fire_new)
        if(!optimizedJC) navigationView?.menu?.findItem(R.id.navigation_junk_cleaner)?.icon = resources.getDrawable(R.drawable.ic_tab_delete_fire_new)
    }

    fun setUpBottomNavWithViewPager(){
        navigationView?.menu?.findItem(R.id.navigation_phone_booster)?.isEnabled = false
        navigationView?.setOnNavigationItemSelectedListener  {
            //Log.e("NavSetSelected", it.toString())
            when(it.itemId){
                R.id.navigation_battery_saver -> viewPager?.currentItem = 1
                R.id.navigation_phone_booster -> viewPager?.currentItem = 0
                R.id.navigation_junk_cleaner -> viewPager?.currentItem = 3
                R.id.navigation_optimizer -> viewPager?.currentItem = 2
            }
            false
        }
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
        block = true
    }

    fun onBottomBar(){
        navigationView?.menu?.findItem(R.id.navigation_phone_booster)?.isEnabled = true
        navigationView?.menu?.findItem(R.id.navigation_optimizer)?.isEnabled = true
        navigationView?.menu?.findItem(R.id.navigation_junk_cleaner)?.isEnabled = true
        navigationView?.menu?.findItem(R.id.navigation_battery_saver)?.isEnabled = true
        block = false
    }

    fun unblockAllExcept(except: Screen){
        onBottomBar()
        if(except == Screen.PHONE_BOOSTER) navigationView?.menu?.findItem(R.id.navigation_phone_booster)?.isEnabled = false
        if(except == Screen.OPTIMIZER) navigationView?.menu?.findItem(R.id.navigation_optimizer)?.isEnabled = false
        if(except == Screen.JUNK_CLEANER) navigationView?.menu?.findItem(R.id.navigation_junk_cleaner)?.isEnabled = false
        if(except == Screen.BATTERY_SAVER) navigationView?.menu?.findItem(R.id.navigation_battery_saver)?.isEnabled = false
    }

    fun customExitDialog(){
//        val dialog = Dialog(this)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setContentView(R.layout.custom_exit_dialog)
//        val v = window.decorView
//        v.setBackgroundResource(android.R.color.transparent)
//        dialog.show()
        val dialog = ExitDialogFragment()
        dialog.show(supportFragmentManager, "exit")
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        if(block) return
        if(findNavController(R.id.kek).currentDestination?.id == R.id.resultFragment) super.onBackPressed()
        else {
            if(optimizedPB && optimizedJC && optimizedOpt && optimizedBS) {
                finish()
            }
            else customExitDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        App.activityResumed()
    }

    override fun onPause() {
        super.onPause()
        App.activityPaused()
    }

    override fun onStop() {
        super.onStop()
        viewPager = null
    }
}

