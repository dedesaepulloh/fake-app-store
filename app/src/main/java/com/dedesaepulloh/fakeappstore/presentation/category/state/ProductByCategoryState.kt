package com.dedesaepulloh.fakeappstore.presentation.category.state

import com.dedesaepulloh.fakeappstore.domain.model.Product

sealed class ProductCategoryState {
    data object Idle : ProductCategoryState()
    data object Loading : ProductCategoryState()
    data class Success(val data: List<Product>) : ProductCategoryState()
    data class Error(val message: String) : ProductCategoryState()
}
