package com.missclickads.cleaner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainScreenFragment : Fragment() {

    var viewPager : ViewPager2? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_screen, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



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
    }
}