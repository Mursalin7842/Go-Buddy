package mursalin.companion.gobuddy.domain.use_case.auth

import mursalin.companion.gobuddy.domain.repository.AuthRepository
import javax.inject.Inject

class ForgetPasswordUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String): Result<Unit> {
        if (email.isBlank()) {
            return Result.failure(Exception("Email cannot be empty."))
        }
        return repository.requestPasswordReset(email)
    }
}
