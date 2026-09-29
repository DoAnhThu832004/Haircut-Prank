package com.example.comthupohaircut.domain.repository

interface UserPreferencesRepository {
    fun isIntroDone(): Boolean
    fun setIntroDone(done: Boolean)
    fun isSoundBaselineDone(): Boolean
    fun setSoundBaselineDone(done: Boolean)
    fun markSoundsKnown(keys: Collection<String>)
    fun isSoundKnown(key: String): Boolean
    fun isNewCategoryViewed(categoryKey: String): Boolean
    fun markNewCategoryViewed(categoryKey: String)
}