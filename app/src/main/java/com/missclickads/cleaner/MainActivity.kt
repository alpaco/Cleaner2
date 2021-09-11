package com.missclickads.cleaner

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
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

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        navigationView?.menu?.findItem(R.id.navigation_phone_booster)?.isEnabled = false

        //todo all
        //navigationView?.menu?.findItem(R.id.navigation_junk_cleaner)?.setIcon(R.drawable.ic_tabjc)
        supportActionBar?.hide()
       // window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        navigationView?.itemIconTintList = null
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_phone_booster, R.id.navigation_battery_saver, R.id.navigation_optimizer, R.id.navigation_junk_cleaner))

        setupActionBarWithNavController(navController, appBarConfiguration)
        navigationView?.setupWithNavController(navController)
    }

    fun optimizeSmth(type: Screen){
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
}

