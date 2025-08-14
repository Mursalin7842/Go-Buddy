package mursalin.companion.gobuddy.domain.use_case.ai

import com.google.firebase.ai.client.generativeai.FirebaseGenerativeAI
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import javax.inject.Inject
import kotlin.Result

@Serializable
data class AiTask(
    val title: String,
    val duration: String
)

@Serializable
data class AiProjectPlan(
    val title: String,
    val description: String,
    val tasks: List<AiTask>
)

class GenerateProjectPlanUseCase @Inject constructor(
    private val generativeAI: FirebaseGenerativeAI
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend operator fun invoke(idea: String): Result<AiProjectPlan> {
        return try {
            val model = generativeAI.generativeModel(
                modelName = "gemini-1.5-flash",
                apiKey = mursalin.companion.gobuddy.BuildConfig.GEMINI_API_KEY
            )

            val prompt = """
                You are a project management assistant. Based on the following user idea, generate a project plan.
                The user's idea is: "$idea".

                Your response must be a single, valid JSON object. Do not include any text, code block markers, or formatting before or after the JSON object.

                The JSON object must have the following structure:
                {
                  "title": "A concise and catchy title for the project",
                  "description": "A one or two sentence description of the project",
                  "tasks": [
                    {
                      "title": "A specific, actionable task to be done",
                      "duration": "An estimated duration, e.g., '2 hours', '1 day', '3 days'"
                    }
                  ]
                }

                Generate at least 5 tasks for the project.
            """.trimIndent()

            val response = model.generateContent(prompt)

            val responseText = response.text
            if (responseText.isNullOrBlank()) {
                return Result.failure(Exception("Received an empty response from the AI."))
            }

            // Clean up the response to ensure it's a valid JSON string
            val cleanedJson = if (responseText.trim().startsWith("{")) {
                responseText.trim()
            } else {
                responseText.substringAfter("{").substringBeforeLast("}").let { "{${it}}" }
            }

            val plan = json.decodeFromString<AiProjectPlan>(cleanedJson)
            Result.success(plan)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
