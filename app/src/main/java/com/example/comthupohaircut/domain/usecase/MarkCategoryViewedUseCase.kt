package com.example.comthupohaircut.domain.usecase

import com.example.comthupohaircut.domain.repository.UserPreferencesRepository
import javax.inject.Inject
class MarkCategoryViewedUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(categoryKey: String) {
        userPreferencesRepository.markNewCategoryViewed(categoryKey)
    }
}