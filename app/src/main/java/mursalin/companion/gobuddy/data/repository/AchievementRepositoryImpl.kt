package mursalin.companion.gobuddy.data.repository

import io.appwrite.services.Databases
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import mursalin.companion.gobuddy.data.repository.AppwriteConstants.ACHIEVEMENTS_COLLECTION_ID
import mursalin.companion.gobuddy.data.repository.AppwriteConstants.DB_ID
import mursalin.companion.gobuddy.domain.model.Achievement
import mursalin.companion.gobuddy.domain.repository.AchievementRepository


class AchievementRepositoryImpl @Inject constructor(
    private val databases: Databases
) : AchievementRepository {

    // Corrected the date format to handle timezone offsets like "+00:00"
    private val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())

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
                    dateEarned = isoFormat.parse(document.data["dateEarned"] as String) ?: Date()
                )
            }
            Result.success(achievements)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
