package com.axelia.yelpprototype

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.axelia.yelpprototype.utils.isNight
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@HiltAndroidApp
class YelpApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val mode = if (isNight()) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }

        AppCompatDelegate.setDefaultNightMode(mode)
    }
}