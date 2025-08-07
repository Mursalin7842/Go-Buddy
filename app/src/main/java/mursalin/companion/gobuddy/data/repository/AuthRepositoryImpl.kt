package mursalin.companion.gobuddy.data.repository

import io.appwrite.ID
import io.appwrite.services.Account
import javax.inject.Inject
import mursalin.companion.gobuddy.domain.model.User
import mursalin.companion.gobuddy.domain.repository.AuthRepository

class AuthRepositoryImpl @Inject constructor(
    private val account: Account
) : AuthRepository {

    override suspend fun signUp(name: String, email: String, password: String): Result<User> {
        return try {
            account.create(
                userId = ID.unique(),
                email = email,
                password = password,
                name = name
            )
            // After signup, log the user in to create a session
            login(email, password)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            account.createEmailPasswordSession(email, password)
            getCurrentUser()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun requestPasswordReset(email: String): Result<Unit> {
        return try {
            // NOTE: Replace with your actual deep link URL for production
            val recoveryUrl = "http://localhost/reset-password"
            account.createRecovery(email, recoveryUrl)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            account.deleteSession("current")
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): Result<User> {
        return try {
            val appwriteUser = account.get()
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
}
