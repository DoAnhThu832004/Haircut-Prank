package com.example.comthupohaircut.presentation.screens.listsound

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comthupohaircut.domain.model.Sound
import com.example.comthupohaircut.domain.usecase.GetSoundsByCategoryUseCase
import com.example.comthupohaircut.domain.usecase.MarkSoundsViewedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListSoundViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getSoundsByCategoryUseCase: GetSoundsByCategoryUseCase,
    private val markSoundsViewedUseCase: MarkSoundsViewedUseCase
): ViewModel() {
    val categoryName: String = savedStateHandle.get<String>("categoryName").orEmpty()

    private val _uiState = MutableStateFlow<ListSoundState>(ListSoundState.Loading)
    val uiState: StateFlow<ListSoundState> = _uiState.asStateFlow()

    private val _effectChannel = Channel<ListSoundEffect>(Channel.BUFFERED)
    val effectFlow = _effectChannel.receiveAsFlow()

    private var hasMarkedKnown = false

    init {
        loadSounds()
    }
    private fun loadSounds() {
        viewModelScope.launch {
            _uiState.value = ListSoundState.Loading
            getSoundsByCategoryUseCase(categoryName)
                .catch { e->
                    _uiState.value = ListSoundState.Error(e.localizedMessage ?: "Failed to load sounds")
                }
                .collect { sounds ->
                    _uiState.value = ListSoundState.Success(
                        categoryName = categoryName,
                        sounds = sounds
                    )
                    if (!hasMarkedKnown && sounds.isNotEmpty()) {
                        hasMarkedKnown = true
                        val keys = sounds.map { it.stableKey }
                        markSoundsViewedUseCase(keys)
                    }
                }
        }
    }
    fun onSoundClick(sound: Sound) {
        viewModelScope.launch {
            _effectChannel.send(ListSoundEffect.NavigateToDetail(sound))
        }
    }
    fun onBackClick() {
        viewModelScope.launch {
            _effectChannel.send(ListSoundEffect.NavigateBack)
        }
    }
}