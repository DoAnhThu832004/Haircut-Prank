package com.example.comthupohaircut.data.local.pref

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
    companion object {
        private const val KEY_INTRO_DONE = "intro_done"
    }
    fun isIntroDone(): Boolean = prefs.getBoolean(KEY_INTRO_DONE,false)
    fun setIntroDone(done: Boolean) {
        prefs.edit().putBoolean(KEY_INTRO_DONE,done).apply()
    }
}