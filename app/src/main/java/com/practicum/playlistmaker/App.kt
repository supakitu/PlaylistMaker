package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        sharedPrefs = getSharedPreferences(PrefsKeys.PLAYLIST_MAKER.key, MODE_PRIVATE)
        val isDarkTheme = sharedPrefs.getBoolean(PrefsKeys.DARK_THEME.key, DARK_THEME_DEFAULT)

        switchTheme(isDarkTheme)
    }

    companion object {
        const val DARK_THEME_DEFAULT = false
        lateinit var sharedPrefs: SharedPreferences
        val gson = Gson()

        fun switchTheme(isDarkTheme: Boolean) {
            if (isDarkTheme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}