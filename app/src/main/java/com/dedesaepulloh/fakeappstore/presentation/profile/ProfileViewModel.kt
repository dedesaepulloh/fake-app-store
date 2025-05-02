package com.dedesaepulloh.fakeappstore.presentation.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dedesaepulloh.fakeappstore.domain.FakeUseCase
import com.dedesaepulloh.fakeappstore.domain.model.User
import com.dedesaepulloh.fakeappstore.presentation.profile.state.ProfileState
import com.dedesaepulloh.fakeappstore.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val useCase: FakeUseCase
) : ViewModel() {

    private val _logoutState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val logoutState: StateFlow<ProfileState> = _logoutState

    private val _userState = MutableStateFlow<User?>(null)
    val userState: StateFlow<User?> = _userState

    private val _isLoginState = MutableStateFlow(false)
    val isLoginState: StateFlow<Boolean> = _isLoginState

    fun getUser() {
        viewModelScope.launch {
            _userState.value = useCase.getUser()
        }
    }

     fun isLoggedIn() {
        viewModelScope.launch {
            _isLoginState.value = useCase.isLoggedIn()
        }
    }

    fun logout() {
        viewModelScope.launch {
            _logoutState.value = ProfileState.Loading
            when (val result = useCase.logout()) {
                is Result.Success -> {
                    _logoutState.value = ProfileState.Success(result.data)
                }

                is Result.Error -> {
                    _logoutState.value = ProfileState.Error(result.message)
                }
            }
        }
    }

    fun resetLogoutState() {
        _logoutState.value = ProfileState.Idle
    }

}