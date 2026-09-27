package com.example.comthupohaircut.domain.usecase
import com.example.comthupohaircut.data.local.pref.AppPreferences
import javax.inject.Inject

class CompleteIntroUseCase @Inject constructor(
    private val appPreferences: AppPreferences
) {
    operator fun invoke() {
        appPreferences.setIntroDone(true)
    }
}