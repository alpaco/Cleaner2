package com.missclickads.cleaner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.missclickads.cleaner.adapters.ViewPagerAdapter
import com.missclickads.cleaner.ui.result.TO

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
        act = (activity as MainActivity)
        viewPager = view.findViewById<ViewPager2>(R.id.viewPager2)
        val viewPagerAdapter = ViewPagerAdapter(act!!)
        viewPager!!.adapter = viewPagerAdapter

        act!!.setupViewPagerFromFragment(viewPager!!)
        viewPager!!.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                choosePosition(position)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        to?.let {
//            viewPager?.currentItem = it
//            choosePosition(it)
        }
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