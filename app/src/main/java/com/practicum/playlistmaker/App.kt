package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    companion object {
        const val DARK_THEME_DEFAULT = false
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
        const val DARK_THEME_KEY = "dark_theme_key"

        fun switchTheme(darkThemeEnabled: Boolean) {
            if (darkThemeEnabled) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        val darkThemeEnabled = sharedPrefs.getBoolean(DARK_THEME_KEY, DARK_THEME_DEFAULT)

        switchTheme(darkThemeEnabled)
    }
}