package com.dedesaepulloh.fakeappstore.presentation.detail.state

import com.dedesaepulloh.fakeappstore.domain.model.Product

sealed class DetailState {
    data object Idle : DetailState()
    data object Loading : DetailState()
    data class Success(val data: Product?) : DetailState()
    data class Error(val message: String) : DetailState()
}
