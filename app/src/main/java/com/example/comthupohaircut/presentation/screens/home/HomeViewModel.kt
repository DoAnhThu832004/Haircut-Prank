package com.example.comthupohaircut.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comthupohaircut.domain.model.SoundCategory
import com.example.comthupohaircut.domain.usecase.GetCategoriesUseCase
import com.example.comthupohaircut.domain.usecase.MarkCategoryViewedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getCategoriesUseCase: GetCategoriesUseCase,
    private val markCategoryViewedUseCase: MarkCategoryViewedUseCase
) : ViewModel() {
    val uiState: StateFlow<HomeUiState> = getCategoriesUseCase()
        .map { categories ->
            HomeUiState(isLoading = false, categories = categories)
        }
        .catch { throwable ->
            emit(HomeUiState(isLoading = false, errorMessage = throwable.message))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState(isLoading = true)
        )
    private val _effectChannel = Channel<HomeUiEffect>(Channel.BUFFERED)
    val effectFlow = _effectChannel.receiveAsFlow()

    fun onCategoryClick(category: SoundCategory) {
        viewModelScope.launch {
            markCategoryViewedUseCase(category.normalizedKey)
            _effectChannel.send(HomeUiEffect.NavigateToListSound(category.name))
        }
    }
    fun onSettingsClick() {
        viewModelScope.launch {
            _effectChannel.send(HomeUiEffect.NavigateToSettings)
        }
    }
}