// =================================================================================
// 📁 DATA LAYER
// =================================================================================
// The DATA layer is responsible for providing data to the application. It knows
// *how* to get the data (e.g., from a remote API like Appwrite or a local
// database). It implements the repository interfaces defined in the domain layer.
// =================================================================================

// ---------------------------------------------------------------------------------
// 📦 data/repository
// ---------------------------------------------------------------------------------
// This package contains the concrete implementations of the repository interfaces.
// ---------------------------------------------------------------------------------

// FILE: app/src/main/java/mursalin/companion/gobuddy/data/repository/AuthRepositoryImpl.kt
package mursalin.companion.gobuddy.data.repository

import io.appwrite.ID
import io.appwrite.services.Account
import mursalin.companion.gobuddy.domain.model.User
import mursalin.companion.gobuddy.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * The concrete implementation of the AuthRepository.
 * This class handles the actual communication with the Appwrite backend for authentication.
 * @param account The Appwrite Account service, injected by Hilt.
 */
class AuthRepositoryImpl @Inject constructor(
    private val account: Account
) : AuthRepository {

    override suspend fun signUp(name: String, email: String, password: String): Result<User> {
        return try {
            // Create the user account in Appwrite
            account.create(
                userId = ID.unique(),
                email = email,
                password = password,
                name = name
            )
            // After creating the account, immediately log the user in to get a session
            login(email, password)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            // Create a session (log the user in)
            account.createEmailPasswordSession(email, password)
            // Fetch the logged-in user's account details
            val appwriteUser = account.get()
            // Map the Appwrite user model to our domain model
            val domainUser = User(
                id = appwriteUser.id,
                name = appwriteUser.name,
                email = appwriteUser.email
            )
            Result.success(domainUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun requestPasswordReset(email: String): Result<Unit> {
        return try {
            // IMPORTANT: Replace with your app's URL for password recovery
            val recoveryUrl = "https://your-app.com/reset-password"
            account.createRecovery(email, recoveryUrl)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            // Delete the current session to log the user out
            account.deleteSession("current")
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}