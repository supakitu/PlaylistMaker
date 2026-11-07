package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    companion object {
        const val DARK_THEME_DEFAULT = false

        fun switchTheme(isDarkTheme: Boolean) {
            if (isDarkTheme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(PrefsKeys.PLAYLIST_MAKER.key, MODE_PRIVATE)
        val isDarkTheme = sharedPrefs.getBoolean(PrefsKeys.DARK_THEME.key, DARK_THEME_DEFAULT)

        switchTheme(isDarkTheme)
    }
}