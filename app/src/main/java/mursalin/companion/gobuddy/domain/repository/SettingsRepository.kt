package mursalin.companion.gobuddy.domain.repository

import kotlinx.coroutines.flow.Flow
import mursalin.companion.gobuddy.domain.model.Theme

/**
 * Interface for the settings repository. Defines the contract for managing
 * user preferences like the application theme.
 */
interface SettingsRepository {
    val theme: Flow<Theme>
    suspend fun setTheme(theme: Theme)
}