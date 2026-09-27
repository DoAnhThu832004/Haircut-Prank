package com.example.comthupohaircut.presentation.screens.intro

sealed interface IntroUiEffect {
    data object NavigateToMain : IntroUiEffect
}