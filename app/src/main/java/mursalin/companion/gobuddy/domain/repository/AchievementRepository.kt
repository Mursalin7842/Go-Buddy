package mursalin.companion.gobuddy.domain.repository

import mursalin.companion.gobuddy.domain.model.Achievement

interface AchievementRepository {
    suspend fun getAchievements(): Result<List<Achievement>>
}