package com.example.comthupohaircut.data.repository

import com.example.comthupohaircut.data.local.pref.AppPreferences
import com.example.comthupohaircut.domain.repository.UserPreferencesRepository
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    private val appPreferences: AppPreferences
) : UserPreferencesRepository {
    override fun isIntroDone(): Boolean = appPreferences.isIntroDone()
    override fun setIntroDone(done: Boolean) = appPreferences.setIntroDone(done)
    override fun isSoundBaselineDone(): Boolean = appPreferences.isSoundBaselineDone()
    override fun setSoundBaselineDone(done: Boolean) = appPreferences.setSoundBaselineDone(done)
    override fun markSoundsKnown(keys: Collection<String>) = appPreferences.markSoundsKnown(keys)
    override fun isSoundKnown(key: String): Boolean = appPreferences.isSoundKnown(key)
    override fun isNewCategoryViewed(categoryKey: String): Boolean = appPreferences.isNewCategoryViewed(categoryKey)
    override fun markNewCategoryViewed(categoryKey: String) = appPreferences.markNewCategoryViewed(categoryKey)
}