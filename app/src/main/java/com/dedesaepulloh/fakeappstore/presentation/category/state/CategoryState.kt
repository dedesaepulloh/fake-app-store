package com.dedesaepulloh.fakeappstore.presentation.category.state

sealed class CategoryState {
    data object Idle : CategoryState()
    data object Loading : CategoryState()
    data class Success(val data: List<String>) : CategoryState()
    data class Error(val message: String) : CategoryState()
}