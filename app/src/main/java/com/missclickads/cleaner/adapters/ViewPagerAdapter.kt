package com.missclickads.cleaner.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.missclickads.cleaner.R
import com.missclickads.cleaner.ui.batterysaver.BatterySaverFragment
import com.missclickads.cleaner.ui.junkcleaner.JunkCleanerFragment
import com.missclickads.cleaner.ui.optimizer.OptimizerFragment
import com.missclickads.cleaner.ui.phonebooster.PhoneBoosterFragment

//class ViewPagerAdapter : RecyclerView.Adapter<PagerVH>() {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
//        PagerVH(LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false))
//
//
//    override fun onBindViewHolder(holder: PagerVH, position: Int) =
//        holder.itemView.run {
//
//        }
//
//    override fun getItemCount(): Int = 3
//}
//
//class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView)

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                PhoneBoosterFragment()
            }
            1 -> {
                BatterySaverFragment()
            }
            2 -> {
                OptimizerFragment()
            }
            3 -> {
                JunkCleanerFragment()
            }
            else -> {
                PhoneBoosterFragment()
            }
        }
    }

}

class ViewPagerStateAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm){
    override fun getCount(): Int = 4

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> {
                PhoneBoosterFragment()
            }
            1 -> {
                BatterySaverFragment()
            }
            2 -> {
                OptimizerFragment()
            }
            3 -> {
                JunkCleanerFragment()
            }
            else -> {
                PhoneBoosterFragment()
            }
        }
    }

}

