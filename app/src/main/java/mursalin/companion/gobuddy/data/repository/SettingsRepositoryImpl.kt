package mursalin.companion.gobuddy.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import mursalin.companion.gobuddy.domain.model.Theme
import mursalin.companion.gobuddy.domain.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The concrete implementation of the SettingsRepository.
 * In a real-world app, this would use Jetpack DataStore to persist the settings
 * on the device. For simplicity and study, this implementation holds the setting in memory.
 */
@Singleton
class SettingsRepositoryImpl @Inject constructor() : SettingsRepository {
    // A simple in-memory StateFlow to hold the current theme.
    // Replace with DataStore for a full production implementation.
    private val _theme = MutableStateFlow(Theme.SYSTEM)
    override val theme: Flow<Theme> = _theme

    override suspend fun setTheme(theme: Theme) {
        _theme.value = theme
    }
}
