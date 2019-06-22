package com.egorshustov.contactsdemo.data.source

import android.app.Application
import androidx.appcompat.app.AppCompatActivity

class ThemesRepository(private val application: Application) {
    fun setNextThemeId() {
        var currentThemeId = getCurrentThemeID()
        val nextThemeId = ++currentThemeId % THEMES_COUNT
        val editor =
            application.getSharedPreferences(THEME_PREFERENCES, AppCompatActivity.MODE_PRIVATE).edit()
        editor.putInt(APP_COLOR_THEME, nextThemeId)
        editor.apply()
    }

    fun getCurrentThemeID() = application.getSharedPreferences(
        THEME_PREFERENCES,
        AppCompatActivity.MODE_PRIVATE
    ).getInt(APP_COLOR_THEME, GREEN_THEME_ID)

    companion object {
        const val THEME_PREFERENCES = "com.egorshustov.contactsdemo.THEME_PREFERENCES"
        const val APP_COLOR_THEME = "com.egorshustov.contactsdemo.APP_COLOR_THEME"
        const val GREEN_THEME_ID = 0
        const val BLUE_THEME_ID = 1
        const val THEMES_COUNT = 2
    }
}