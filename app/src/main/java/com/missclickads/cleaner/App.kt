package com.missclickads.cleaner

import android.app.Application


class App : Application() {


    companion object{
        var isActivityVisible = false
        fun activityResumed() {
            isActivityVisible = true
        }

        fun activityPaused() {
            isActivityVisible = false
        }
    }


}