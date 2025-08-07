package mursalin.companion.gobuddy.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import mursalin.companion.gobuddy.domain.use_case.auth.CheckSessionUseCase
import mursalin.companion.gobuddy.domain.use_case.auth.ForgetPasswordUseCase
import mursalin.companion.gobuddy.domain.use_case.auth.LoginUseCase
import mursalin.companion.gobuddy.domain.use_case.auth.SignUpUseCase
import javax.inject.Inject
import mursalin.companion.gobuddy.domain.model.User as DomainUser

// (AuthEvent and AuthState data class remain the same)
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
    object ClearError : AuthEvent()

    object CheckSession : AuthEvent()
}

data class AuthState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: DomainUser? = null
)


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val checkSessionUseCase: CheckSessionUseCase,
    private val loginUseCase: LoginUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val forgetPasswordUseCase: ForgetPasswordUseCase
) : ViewModel() {

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
            is AuthEvent.ClearError -> _state.update { it.copy(error = null) }
            is AuthEvent.CheckSession -> checkUserSession()
        }
    }

    private fun loginUser() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = loginUseCase(
                email = _state.value.email,
                password = _state.value.password
            )
            result.onSuccess { user ->
                _state.update { it.copy(isLoading = false, user = user) }
            }.onFailure { exception ->
                _state.update { it.copy(isLoading = false, error = exception.message) }
            }
        }
    }

    private fun signUpUser() {
        viewModelScope.launch {
            if (_state.value.password != _state.value.confirmPassword) {
                _state.update { it.copy(error = "Passwords do not match.") }
                return@launch
            }
            _state.update { it.copy(isLoading = true, error = null) }
            val result = signUpUseCase(
                name = _state.value.fullName,
                email = _state.value.email,
                password = _state.value.password
            )
            result.onSuccess { user ->
                _state.update { it.copy(isLoading = false, user = user) }
            }.onFailure { exception ->
                _state.update { it.copy(isLoading = false, error = exception.message) }
            }
        }
    }

    private fun resetPassword() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val result = forgetPasswordUseCase(_state.value.email)
            result.onSuccess {
                _state.update { it.copy(isLoading = false, error = "Password reset link sent to your email.") }
            }.onFailure { exception ->
                _state.update { it.copy(isLoading = false, error = exception.message) }
            }
        }
    }
    private fun checkUserSession() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            checkSessionUseCase()
                .onSuccess { user ->
                    _state.update { it.copy(isLoading = false, user = user) }
                }
                .onFailure {
                    _state.update { it.copy(isLoading = false, user = null) }
                }
        }
    }
}
