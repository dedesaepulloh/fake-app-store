package com.dedesaepulloh.fakeappstore.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dedesaepulloh.fakeappstore.domain.FakeUseCase
import com.dedesaepulloh.fakeappstore.domain.model.User
import com.dedesaepulloh.fakeappstore.presentation.register.state.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val useCase: FakeUseCase
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    private val _isFormValid = MutableStateFlow(false)
    val isFormValid: StateFlow<Boolean> = _isFormValid

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    private val _nameError = MutableStateFlow<String?>(null)
    val nameError: StateFlow<String?> = _nameError

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError

    private val _addressError = MutableStateFlow<String?>(null)
    val addressError: StateFlow<String?> = _addressError

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError

    private val _confirmPasswordError = MutableStateFlow<String?>(null)
    val confirmPasswordError: StateFlow<String?> = _confirmPasswordError

    fun onNameChange(newName: String) {
        _name.value = newName
        validateForm()
    }

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
        validateForm()
    }

    fun onAddressChange(newAddress: String) {
        _address.value = newAddress
        validateForm()
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
        validateForm()
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _confirmPassword.value = newConfirmPassword
        validateForm()
    }

    private fun validateForm() {
        val nameValid = _name.value.isNotEmpty()
        val emailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()
        val addressValid = _address.value.isNotEmpty()
        val passwordValid = _password.value.length >= 8 &&
                _password.value.any { it.isUpperCase() } &&
                _password.value.any { it.isLowerCase() } &&
                _password.value.any { it.isDigit() } &&
                _password.value.any { !it.isLetterOrDigit() }
        val confirmPasswordValid = _confirmPassword.value == _password.value

        _nameError.value = if (!nameValid) "Name is required" else null
        _emailError.value = if (!emailValid) "Invalid email address" else null
        _addressError.value = if (!addressValid) "Address is required" else null
        _passwordError.value = when {
            _password.value.isEmpty() -> "Password cannot be empty"
            _password.value.length < 8 -> "Password must be at least 8 characters"
            !_password.value.any { it.isUpperCase() } -> "Password must contain at least one uppercase letter"
            !_password.value.any { it.isLowerCase() } -> "Password must contain at least one lowercase letter"
            !_password.value.any { it.isDigit() } -> "Password must contain at least one number"
            !_password.value.any { !it.isLetterOrDigit() } -> "Password must contain at least one special character"
            else -> null
        }
        _confirmPasswordError.value = if (!confirmPasswordValid) "Passwords do not match" else null

        _isFormValid.value =
            nameValid && emailValid && addressValid && passwordValid && confirmPasswordValid
    }

    fun register(
        name: String,
        email: String,
        address: String,
        password: String,
        confirmPassword: String
    ) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            try {
                val user = User(
                    email, password, name, address
                )
                useCase.insert(user)
                _registerState.value = RegisterState.Success("Registration Successful")
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun resetState() {
        _registerState.value = RegisterState.Idle
    }

}