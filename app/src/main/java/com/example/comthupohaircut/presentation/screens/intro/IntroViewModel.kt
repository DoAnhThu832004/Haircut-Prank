package com.example.comthupohaircut.presentation.screens.intro

import androidx.lifecycle.ViewModel
import com.example.comthupohaircut.domain.usecase.CompleteIntroUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val completeIntroUseCase: CompleteIntroUseCase
): ViewModel() {
    fun onIntroCompleted(onNavigateToMain: () -> Unit) {
        completeIntroUseCase()
        onNavigateToMain()
    }
}