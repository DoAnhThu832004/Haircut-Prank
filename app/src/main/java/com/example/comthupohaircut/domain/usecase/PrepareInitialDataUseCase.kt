package com.example.comthupohaircut.domain.usecase

import com.example.comthupohaircut.data.local.pref.AppPreferences
import javax.inject.Inject

class PrepareInitialDataUseCase @Inject constructor(
    private val appPreferences: AppPreferences
) {
    suspend operator fun invoke(): Result<Boolean> {
        return runCatching {
            !appPreferences.isIntroDone()
        }
    }
}