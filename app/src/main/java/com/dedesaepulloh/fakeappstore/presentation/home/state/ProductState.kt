package com.dedesaepulloh.fakeappstore.presentation.home.state

import com.dedesaepulloh.fakeappstore.domain.model.Product

sealed class ProductState {
    data object Idle : ProductState()
    data object Loading : ProductState()
    data class Success(val products: List<Product>) : ProductState()
    data class Error(val message: String) : ProductState()
}
