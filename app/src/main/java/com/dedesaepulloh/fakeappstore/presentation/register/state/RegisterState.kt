package com.dedesaepulloh.fakeappstore.presentation.register.state

sealed class RegisterState {
    data object Idle : RegisterState()
    data object Loading : RegisterState()
    data class Success(val data: String) : RegisterState()
    data class Error(val message: String) : RegisterState()
}