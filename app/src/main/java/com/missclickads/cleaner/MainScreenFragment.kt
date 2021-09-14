package com.missclickads.cleaner

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.missclickads.cleaner.adapters.ViewPagerAdapter
import com.missclickads.cleaner.ui.result.TO
import com.missclickads.cleaner.utils.Screen

class MainScreenFragment : Fragment(R.layout.fragment_main_screen) {

    var viewPager : ViewPager2? = null
    private var to : Int? = null
    var act : MainActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            to = it.getInt(TO)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("MainScreen", "OnViewCreated")
        act = (activity as MainActivity)
        viewPager = view.findViewById<ViewPager2>(R.id.viewPager2)
        val viewPagerAdapter = ViewPagerAdapter(act!!)
        viewPager!!.adapter = viewPagerAdapter
        viewPager!!.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                choosePosition(position)
            }
        })
        act!!.setupViewPagerFromFragment(viewPager!!)
        if(to != null){
            when (to){
                0-> act!!.unblockAllExcept(Screen.PHONE_BOOSTER)
                1-> act!!.unblockAllExcept(Screen.BATTERY_SAVER)
                2-> act!!.unblockAllExcept(Screen.OPTIMIZER)
                3-> act!!.unblockAllExcept(Screen.JUNK_CLEANER)
            }
            Log.e("MainScreen", to.toString())
            //Хз почему, но без задержки нихуя не работает
            viewPager!!.postDelayed(Runnable {
                viewPager!!.currentItem = to!!
            }, 10)

        }
    }

    override fun onResume() {
        super.onResume()
        Log.e("MainScreen", "OnResume")
        act?.navigationView?.visibility = View.VISIBLE
        viewPager?.let { act?.setupViewPagerFromFragment(it) }
    }

    fun choosePosition(position: Int){
        when(position){
            0 -> {
                act!!.navigationView?.menu?.findItem(R.id.navigation_phone_booster)?.isChecked = true
            }//navController.navigate(R.id.navigation_phone_booster)
            1 -> {
                //navController.navigate(R.id.navigation_battery_saver)
                act!!.navigationView?.menu?.findItem(R.id.navigation_battery_saver)?.isChecked = true
            }
            2 -> {
                // navController.navigate(R.id.navigation_optimizer)
                act!!.navigationView?.menu?.findItem(R.id.navigation_optimizer)?.isChecked = true
            }
            3 -> {
                //navController.navigate(R.id.navigation_junk_cleaner)
                act!!.navigationView?.menu?.findItem(R.id.navigation_junk_cleaner)?.isChecked = true
            }
        }
    }
}