package mursalin.companion.gobuddy.data.repository

import io.appwrite.services.Databases
import mursalin.companion.gobuddy.domain.model.Achievement
import mursalin.companion.gobuddy.domain.repository.AchievementRepository
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

const val ACHIEVEMENTS_COLLECTION_ID = "achievements"

class AchievementRepositoryImpl @Inject constructor(
    private val databases: Databases
) : AchievementRepository {

    private val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

    override suspend fun getAchievements(): Result<List<Achievement>> {
        return try {
            val response = databases.listDocuments(
                databaseId = DB_ID,
                collectionId = ACHIEVEMENTS_COLLECTION_ID
            )
            val achievements = response.documents.map { document ->
                Achievement(
                    id = document.id,
                    title = document.data["title"] as String,
                    description = document.data["description"] as String,
                    iconName = document.data["iconName"] as String,
                    dateEarned = (document.data["dateEarned"] as? String)?.let { isoFormat.parse(it) }
                )
            }
            Result.success(achievements)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}