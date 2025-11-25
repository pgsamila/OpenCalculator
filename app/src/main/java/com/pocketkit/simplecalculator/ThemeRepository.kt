package com.pocketkit.simplecalculator

import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    val theme: Flow<AppTheme>
    suspend fun setTheme(theme: AppTheme)
}
