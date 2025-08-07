package mursalin.companion.gobuddy.domain.repository

import mursalin.companion.gobuddy.domain.model.User

interface AuthRepository {
    suspend fun signUp(name: String, email: String, password: String): Result<User>
    suspend fun login(email: String, password: String): Result<User>
    suspend fun requestPasswordReset(email: String): Result<Unit>
    suspend fun logout(): Result<Unit>
    suspend fun getCurrentUser(): Result<User>
}
