package mursalin.companion.gobuddy.domain.repository

import mursalin.companion.gobuddy.domain.model.User

/**
 * Interface for the authentication repository.
 * It defines the contract for all authentication-related data operations.
 * The ViewModel will interact with this interface, not the implementation.
 */
interface AuthRepository {

    /**
     * Signs up a new user.
     * @param name The user's full name.
     * @param email The user's email address.
     * @param password The user's chosen password.
     * @return A Result containing the signed-up User on success, or an Exception on failure.
     */
    suspend fun signUp(name: String, email: String, password: String): Result<User>

    /**
     * Logs in an existing user.
     * @param email The user's email address.
     * @param password The user's password.
     * @return A Result containing the logged-in User on success, or an Exception on failure.
     */
    suspend fun login(email: String, password: String): Result<User>

    /**
     * Sends a password reset link to the user's email.
     * @param email The user's email address.
     * @return A Result indicating success, or an Exception on failure.
     */
    suspend fun requestPasswordReset(email: String): Result<Unit>

    /**
     * Logs out the current user.
     * @return A Result indicating success, or an Exception on failure.
     */
    suspend fun logout(): Result<Unit>
}
