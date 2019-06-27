package com.egorshustov.contactsdemo.data.source

import android.content.Context
import android.preference.PreferenceManager.getDefaultSharedPreferences

class ThemesRepository(private val context: Context) {
    fun setNextThemeId() {
        var currentThemeId = getCurrentThemeID()
        val nextThemeId = ++currentThemeId % THEMES_COUNT
        val editor = getDefaultSharedPreferences(context).edit()
        editor.putInt(APP_COLOR_THEME, nextThemeId)
        editor.apply()
    }

    fun getCurrentThemeID() = getDefaultSharedPreferences(context).getInt(APP_COLOR_THEME, GREEN_THEME_ID)

    companion object {
        const val APP_COLOR_THEME = "com.egorshustov.contactsdemo.APP_COLOR_THEME"
        const val GREEN_THEME_ID = 0
        const val BLUE_THEME_ID = 1
        const val THEMES_COUNT = 2
    }
}