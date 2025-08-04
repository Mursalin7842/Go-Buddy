package mursalin.companion.gobuddy.domain.repository

import kotlinx.coroutines.flow.Flow
import mursalin.companion.gobuddy.domain.model.Theme

interface SettingsRepository {
    val theme: Flow<Theme>
    suspend fun setTheme(theme: Theme)
}
