package com.dedesaepulloh.fakeappstore.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dedesaepulloh.fakeappstore.domain.FakeUseCase
import com.dedesaepulloh.fakeappstore.presentation.detail.state.DetailState
import com.dedesaepulloh.fakeappstore.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val useCase: FakeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<DetailState>(DetailState.Idle)
    val state: StateFlow<DetailState> = _state

    fun getDetail(id: Int) {
        viewModelScope.launch {
            _state.value = DetailState.Loading
            when (val result = useCase.getProductDetail(id)) {
                is Result.Success -> _state.value = DetailState.Success(result.data)
                is Result.Error -> _state.value = DetailState.Error(result.message)
            }
        }
    }

}