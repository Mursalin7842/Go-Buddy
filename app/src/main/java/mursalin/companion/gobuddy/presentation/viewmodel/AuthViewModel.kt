package mursalin.companion.gobuddy.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Represents all possible user actions on the authentication screens.
sealed class AuthEvent {
    data class FullNameChanged(val value: String) : AuthEvent()
    data class EmailChanged(val value: String) : AuthEvent()
    data class PasswordChanged(val value: String) : AuthEvent()
    data class ConfirmPasswordChanged(val value: String) : AuthEvent()
    object TogglePasswordVisibility : AuthEvent()
    object ToggleConfirmPasswordVisibility : AuthEvent()
    object Login : AuthEvent()
    object SignUp : AuthEvent()
    object ResetPassword : AuthEvent()
}

// Holds the entire state for the authentication UI.
data class AuthState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthSuccessful: Boolean = false
)

class AuthViewModel : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.FullNameChanged -> _state.update { it.copy(fullName = event.value) }
            is AuthEvent.EmailChanged -> _state.update { it.copy(email = event.value) }
            is AuthEvent.PasswordChanged -> _state.update { it.copy(password = event.value) }
            is AuthEvent.ConfirmPasswordChanged -> _state.update { it.copy(confirmPassword = event.value) }
            is AuthEvent.TogglePasswordVisibility -> _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            is AuthEvent.ToggleConfirmPasswordVisibility -> _state.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
            is AuthEvent.Login -> loginUser()
            is AuthEvent.SignUp -> signUpUser()
            is AuthEvent.ResetPassword -> resetPassword()
        }
    }

    private fun loginUser() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            // In a real app, you would call your LoginUseCase here.
            // For now, we'll simulate a successful login.
            delay(2000) // Simulate network call
            _state.update { it.copy(isLoading = false, isAuthSuccessful = true) }
        }
    }

    private fun signUpUser() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            // In a real app, you would call your SignupUseCase here.
            delay(2000)
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun resetPassword() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            // In a real app, you would call your ForgotPasswordUseCase here.
            delay(2000)
            _state.update { it.copy(isLoading = false) }
        }
    }
}
