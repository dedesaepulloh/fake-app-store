package com.dedesaepulloh.fakeappstore.presentation.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dedesaepulloh.fakeappstore.domain.FakeUseCase
import com.dedesaepulloh.fakeappstore.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val useCase: FakeUseCase
) : ViewModel() {

    private val _wishlist = MutableStateFlow<List<Product>>(emptyList())
    val wishlist: StateFlow<List<Product>> = _wishlist

    fun addToWishlist(product: Product) {
        viewModelScope.launch {
            useCase.addToWishlist(product)
        }
    }

    fun removeWishlist(product: Product) {
        viewModelScope.launch {
            useCase.removeWishlist(product)
        }
    }

    suspend fun isWishlist(id: Int): Product? {
        return useCase.isWishlist(id)
    }

    fun loadWishlist() {
        viewModelScope.launch {
            _wishlist.value = useCase.getAllWishlist()
        }
    }


}