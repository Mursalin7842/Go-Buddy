package mursalin.companion.gobuddy.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import mursalin.companion.gobuddy.domain.model.Theme
import mursalin.companion.gobuddy.domain.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor() : SettingsRepository {
    // In a real app, this would be backed by DataStore for persistence.
    // For this example, we use a simple in-memory StateFlow.
    private val _theme = MutableStateFlow(Theme.SYSTEM)
    override val theme: Flow<Theme> = _theme

    override suspend fun setTheme(theme: Theme) {
        _theme.value = theme
    }
}