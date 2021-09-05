package com.missclickads.cleaner.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

const val S_BATTERY_SAVER = "bs"
const val S_JUNK_CLEANER = "jc"
const val S_OPTIMIZER = "opt"
const val S_PHONE_BOOSTER = "pb"

const val S_PREF_NAME = "shpr"
const val DATA_PATTERN = "dd-MMM-yyyy HH:mm:ss"

class OptimizeDataRepository(context: Context) {

    var sPrefs : SharedPreferences = context.getSharedPreferences(S_PREF_NAME, Context.MODE_PRIVATE)

    fun putData(data : Date,screen: Screen){
        val dateFormat: DateFormat = SimpleDateFormat(DATA_PATTERN)
        when(screen){
            Screen.BATTERY_SAVER -> sPrefs.edit().putString(S_BATTERY_SAVER,dateFormat.format(data)).apply()
            Screen.JUNK_CLEANER-> sPrefs.edit().putString(S_JUNK_CLEANER,dateFormat.format(data)).apply()
            Screen.OPTIMIZER -> sPrefs.edit().putString(S_OPTIMIZER,dateFormat.format(data)).apply()
            Screen.PHONE_BOOSTER -> sPrefs.edit().putString(S_PHONE_BOOSTER,dateFormat.format(data)).apply()
        }
    }
    fun getData():OptimizeData{
        return OptimizeData(
            batterySaver = getDataFormat(S_BATTERY_SAVER),
            phoneBooster = getDataFormat(S_PHONE_BOOSTER),
            optimizer = getDataFormat(S_OPTIMIZER),
            junkCleaner = getDataFormat(S_JUNK_CLEANER)
        )
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDataFormat(type : String):Date{
        val formatter = SimpleDateFormat(DATA_PATTERN)
        val dataStr = sPrefs.getString(type,"20-10-2020 12:12:12")
        return formatter.parse(dataStr!!)!!
    }


}

enum class Screen{
    BATTERY_SAVER,
    JUNK_CLEANER,
    OPTIMIZER,
    PHONE_BOOSTER
}

data class OptimizeData(
    val batterySaver : Date,
    val phoneBooster : Date,
    val optimizer : Date,
    val junkCleaner : Date,
)