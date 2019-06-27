package com.egorshustov.contactsdemo.data.source

import android.content.Context
import android.preference.PreferenceManager.getDefaultSharedPreferences
import com.egorshustov.contactsdemo.utils.ThemesUtils.APP_COLOR_THEME
import com.egorshustov.contactsdemo.utils.ThemesUtils.GREEN_THEME_ID
import com.egorshustov.contactsdemo.utils.ThemesUtils.THEMES_COUNT

class ThemesRepository(private val context: Context) {
    fun setNextThemeId() {
        var currentThemeId = getCurrentThemeID()
        val nextThemeId = ++currentThemeId % THEMES_COUNT
        val editor = getDefaultSharedPreferences(context).edit()
        editor.putInt(APP_COLOR_THEME, nextThemeId)
        editor.apply()
    }

    fun getCurrentThemeID() = getDefaultSharedPreferences(context).getInt(APP_COLOR_THEME, GREEN_THEME_ID)
}