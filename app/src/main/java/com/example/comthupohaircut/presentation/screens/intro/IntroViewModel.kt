package com.example.comthupohaircut.presentation.screens.intro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comthupohaircut.domain.usecase.CompleteIntroUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val completeIntroUseCase: CompleteIntroUseCase
): ViewModel() {
    private val _effectChannel = Channel<IntroUiEffect>(Channel.BUFFERED)
    val effectFlow = _effectChannel.receiveAsFlow()

    fun onFinishIntro() {
        viewModelScope.launch {
            completeIntroUseCase()
            _effectChannel.send(IntroUiEffect.NavigateToMain)
        }
    }
}