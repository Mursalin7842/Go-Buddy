package mursalin.companion.gobuddy.domain.use_case.auth

import javax.inject.Inject
import mursalin.companion.gobuddy.domain.repository.AuthRepository

class LogoutUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.logout()
    }
}
