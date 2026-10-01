package com.example.comthupohaircut.presentation.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comthupohaircut.data.audio.AudioPlayerManager
import com.example.comthupohaircut.domain.model.Sound
import com.example.comthupohaircut.domain.usecase.GetFavoriteSoundsUseCase
import com.example.comthupohaircut.domain.usecase.GetSoundByPathUseCase
import com.example.comthupohaircut.domain.usecase.GetSoundsByCategoryUseCase
import com.example.comthupohaircut.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLDecoder
import javax.inject.Inject

@HiltViewModel
class DetailSoundViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getSoundByPathUseCase: GetSoundByPathUseCase,
    private val getSoundsByCategoryUseCase: GetSoundsByCategoryUseCase,
    private val getFavoriteSoundsUseCase: GetFavoriteSoundsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val audioPlayerManager: AudioPlayerManager
) : ViewModel() {

    val categoryName: String = savedStateHandle.get<String>("categoryName").orEmpty()
    private val rawPath: String = savedStateHandle.get<String>("soundPath").orEmpty()
    val initialSoundPath: String = runCatching { URLDecoder.decode(rawPath, "UTF-8") }.getOrDefault(rawPath)

    private val _uiState = MutableStateFlow(DetailSoundUiState())
    val uiState: StateFlow<DetailSoundUiState> = _uiState.asStateFlow()

    private val _effectChannel = Channel<DetailSoundUiEffect>(Channel.BUFFERED)
    val effectFlow = _effectChannel.receiveAsFlow()

    private var countdownJob: Job? = null

    init {
        loadInitialData(initialSoundPath)
        observePlayerState()
    }

    private fun observePlayerState() {
        viewModelScope.launch {
            audioPlayerManager.isPlaying.collect { playing ->
                _uiState.update { it.copy(isPlaying = playing) }
            }
        }
    }

    private fun loadInitialData(targetPath: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val sound = getSoundByPathUseCase(targetPath)
            sound?.let { s ->
                _uiState.update {
                    it.copy(
                        currentSound = s,
                        isFavorite = s.checkFavorite,
                        isLoading = false
                    )
                }
            }

            // Tải danh sách âm thanh liên quan
            if (categoryName == "FAVORITE") {
                getFavoriteSoundsUseCase().collect { list ->
                    _uiState.update { it.copy(otherSounds = list) }
                }
            } else {
                getSoundsByCategoryUseCase(categoryName).collect { list ->
                    _uiState.update { it.copy(otherSounds = list) }
                }
            }
        }
    }

    fun onTogglePlay() {
        val state = _uiState.value
        val sound = state.currentSound ?: return

        when {
            // Đang đếm ngược -> Hủy đếm ngược
            state.isCountingDown -> {
                cancelCountdown()
            }
            // Đang phát -> Dừng
            state.isPlaying -> {
                audioPlayerManager.stop()
            }
            // Đã chọn hẹn giờ -> Bắt đầu đếm ngược
            state.selectedTimerSeconds > 0 -> {
                startCountdown(state.selectedTimerSeconds, sound.pathSound)
            }
            // Phát ngay
            else -> {
                audioPlayerManager.play(sound.pathSound, state.isLooping)
            }
        }
    }

    private fun startCountdown(seconds: Int, path: String) {
        countdownJob?.cancel()
        _uiState.update {
            it.copy(
                isCountingDown = true,
                countdownRemainingSeconds = seconds
            )
        }

        countdownJob = viewModelScope.launch {
            var remaining = seconds
            while (remaining > 0) {
                delay(1000L)
                remaining--
                _uiState.update { it.copy(countdownRemainingSeconds = remaining) }
            }
            // Hết giờ -> kích hoạt phát âm thanh!
            _uiState.update { it.copy(isCountingDown = false) }
            audioPlayerManager.play(path, _uiState.value.isLooping)
        }
    }

    private fun cancelCountdown() {
        countdownJob?.cancel()
        countdownJob = null
        _uiState.update {
            it.copy(
                isCountingDown = false,
                countdownRemainingSeconds = 0
            )
        }
    }

    fun onSelectTimer(seconds: Int) {
        cancelCountdown()
        _uiState.update { it.copy(selectedTimerSeconds = seconds) }
    }

    fun onToggleLoop(loop: Boolean) {
        _uiState.update { it.copy(isLooping = loop) }
        audioPlayerManager.setLoop(loop)
    }

    fun onToggleVibration() {
        val newVibrate = !_uiState.value.isVibrationEnabled
        _uiState.update { it.copy(isVibrationEnabled = newVibrate) }
        audioPlayerManager.setVibrationEnabled(newVibrate)
    }

    fun onToggleFavorite() {
        val sound = _uiState.value.currentSound ?: return
        viewModelScope.launch {
            toggleFavoriteUseCase(sound)
            val updated = sound.copy(checkFavorite = !sound.checkFavorite)
            _uiState.update {
                it.copy(
                    currentSound = updated,
                    isFavorite = updated.checkFavorite
                )
            }
        }
    }

    fun onSelectSound(newSound: Sound) {
        if (_uiState.value.currentSound?.pathSound == newSound.pathSound) return
        cancelCountdown()
        audioPlayerManager.stop()

        _uiState.update {
            it.copy(
                currentSound = newSound,
                isFavorite = newSound.checkFavorite
            )
        }
    }

    fun onBackClick() {
        cancelCountdown()
        audioPlayerManager.stop()
        viewModelScope.launch {
            _effectChannel.send(DetailSoundUiEffect.NavigateBack)
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelCountdown()
        audioPlayerManager.release()
    }
}