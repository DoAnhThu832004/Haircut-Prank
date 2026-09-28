package com.example.comthupohaircut.domain.usecase
import com.example.comthupohaircut.data.local.pref.AppPreferences
import com.example.comthupohaircut.domain.model.SoundCategory
import com.example.comthupohaircut.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository,
    private val appPreferences: AppPreferences
) {
    private val newTagCategories = setOf("siren","taser","scary","animals")
    operator fun invoke(): Flow<List<SoundCategory>> {
        return repository.getAllCategories().map { list ->
            list.map { category ->
                val key = category.normalizedKey
                val shouldShowNew = key in newTagCategories && !appPreferences.isNewCategoryViewed(key)
                category.copy(isNew = shouldShowNew)
            }
        }
    }
}