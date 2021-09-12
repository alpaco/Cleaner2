package com.missclickads.cleaner.custom

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager2.widget.ViewPager2
import android.view.MotionEvent

import androidx.viewpager.widget.ViewPager

//
//class CustomViewPager(context: Context?, attrs: AttributeSet?) :
//    ViewPager2(context, attrs) {
//    private var enabled = true
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        return if (enabled) {
//            super.onTouchEvent(event)
//        } else false
//    }
//
//    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
//        return if (enabled) {
//            super.onInterceptTouchEvent(event)
//        } else false
//    }
//
//    fun setPagingEnabled(enabled: Boolean) {
//        this.enabled = enabled
//    }
//}