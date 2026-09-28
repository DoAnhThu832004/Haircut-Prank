package com.example.comthupohaircut.data.local.pref

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferences @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_INTRO_DONE = "intro_done"
        private const val KEY_KNOWN_SOUNDS = "known_sound_paths"
        private const val KEY_SOUND_BASELINE_DONE = "new_sound_baseline_done"
        private const val KEY_VIEWED_NEW_CATEGORIES = "viewed_new_categories"
    }

    fun isIntroDone(): Boolean = prefs.getBoolean(KEY_INTRO_DONE, false)

    fun setIntroDone(done: Boolean) {
        prefs.edit().putBoolean(KEY_INTRO_DONE, done).apply()
    }

    fun isSoundBaselineDone(): Boolean = prefs.getBoolean(KEY_SOUND_BASELINE_DONE, false)

    fun setSoundBaselineDone(done: Boolean) {
        prefs.edit().putBoolean(KEY_SOUND_BASELINE_DONE, done).apply()
    }

    fun markSoundsKnown(keys: Collection<String>) {
        if (keys.isEmpty()) return
        val current = prefs.getStringSet(KEY_KNOWN_SOUNDS, emptySet())?.toMutableSet() ?: mutableSetOf()
        if (current.addAll(keys)) {
            prefs.edit().putStringSet(KEY_KNOWN_SOUNDS, current).apply()
        }
    }

    fun isSoundKnown(key: String): Boolean {
        val current = prefs.getStringSet(KEY_KNOWN_SOUNDS, emptySet()) ?: emptySet()
        return current.contains(key)
    }

    fun isNewCategoryViewed(categoryKey: String): Boolean {
        val viewedSet = prefs.getStringSet(KEY_VIEWED_NEW_CATEGORIES, emptySet()) ?: emptySet()
        return viewedSet.contains(categoryKey)
    }

    fun markNewCategoryViewed(categoryKey: String) {
        val current = prefs.getStringSet(KEY_VIEWED_NEW_CATEGORIES, emptySet())?.toMutableSet() ?: mutableSetOf()
        if (current.add(categoryKey)) {
            prefs.edit().putStringSet(KEY_VIEWED_NEW_CATEGORIES, current).apply()
        }
    }
}