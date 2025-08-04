package mursalin.companion.gobuddy.domain.use_case.auth

import mursalin.companion.gobuddy.domain.model.User
import mursalin.companion.gobuddy.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(Exception("Email and password cannot be empty."))
        }
        return repository.login(email, password)
    }
}