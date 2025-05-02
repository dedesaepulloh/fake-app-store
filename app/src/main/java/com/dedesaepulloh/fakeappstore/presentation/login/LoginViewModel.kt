package com.dedesaepulloh.fakeappstore.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dedesaepulloh.fakeappstore.domain.FakeUseCase
import com.dedesaepulloh.fakeappstore.presentation.login.state.LoginState
import com.dedesaepulloh.fakeappstore.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val useCase: FakeUseCase
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isFormValid = MutableStateFlow(false)
    val isFormValid: StateFlow<Boolean> = _isFormValid

    private val _isEmailTouched = MutableStateFlow(false)
    private val _isPasswordTouched = MutableStateFlow(false)

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
        _isEmailTouched.value = true
        validateForm()
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
        _isPasswordTouched.value = true
        validateForm()
    }

    private fun validateForm() {
        val emailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()
        val passwordValid = _password.value.length >= 8 &&
                _password.value.any { it.isUpperCase() } &&
                _password.value.any { it.isLowerCase() } &&
                _password.value.any { it.isDigit() } &&
                _password.value.any { !it.isLetterOrDigit() }

        if (_isEmailTouched.value) {
            _emailError.value = if (emailValid) null else "Invalid email address"
        }

        if (_isPasswordTouched.value) {
            _passwordError.value = when {
                _password.value.isEmpty() -> "Password cannot be empty"
                _password.value.length < 8 -> "Password must be at least 8 characters"
                !_password.value.any { it.isUpperCase() } -> "Password must contain at least one uppercase letter"
                !_password.value.any { it.isLowerCase() } -> "Password must contain at least one lowercase letter"
                !_password.value.any { it.isDigit() } -> "Password must contain at least one number"
                !_password.value.any { !it.isLetterOrDigit() } -> "Password must contain at least one special character"
                else -> null
            }
        }

        _isFormValid.value = emailValid && passwordValid
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            when (val result = useCase.login(username, password)) {
                is Result.Success -> {
                    _loginState.value = LoginState.Success(message = result.data)
                }

                is Result.Error -> {
                    _loginState.value = LoginState.Error(result.message)
                }

            }
        }
    }

    fun reserState() {
        _loginState.value = LoginState.Idle
    }

}