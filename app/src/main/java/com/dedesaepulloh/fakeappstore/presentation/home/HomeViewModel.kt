package com.dedesaepulloh.fakeappstore.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dedesaepulloh.fakeappstore.domain.FakeUseCase
import com.dedesaepulloh.fakeappstore.presentation.home.state.ProductState
import com.dedesaepulloh.fakeappstore.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: FakeUseCase
) : ViewModel() {

    private val _productState = MutableStateFlow<ProductState>(ProductState.Idle)
    val productState: StateFlow<ProductState> = _productState

    fun getAllProducts() {
        viewModelScope.launch {
            _productState.value = ProductState.Loading
            when (val result = useCase.getAllProducts()) {
                is Result.Success -> {
                    _productState.value = ProductState.Success(result.data)
                }

                is Result.Error -> {
                    _productState.value = ProductState.Error(result.message)
                }
            }
        }
    }
}