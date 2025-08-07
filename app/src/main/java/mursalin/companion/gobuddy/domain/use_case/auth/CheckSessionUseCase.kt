package mursalin.companion.gobuddy.domain.use_case.auth

import javax.inject.Inject
import mursalin.companion.gobuddy.domain.model.User
import mursalin.companion.gobuddy.domain.repository.AuthRepository

class CheckSessionUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Result<User> {
        return repository.getCurrentUser()
    }
}
