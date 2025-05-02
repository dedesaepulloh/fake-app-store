package com.dedesaepulloh.fakeappstore.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dedesaepulloh.fakeappstore.domain.FakeUseCase
import com.dedesaepulloh.fakeappstore.domain.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val useCase: FakeUseCase
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<Product>>(emptyList())
    val cartItems: StateFlow<List<Product>> = _cartItems.asStateFlow()

    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice.asStateFlow()

    private val _totalCart = MutableStateFlow(0)
    val totalCart: StateFlow<Int> = _totalCart.asStateFlow()

    private val _checkoutProducts = MutableStateFlow<List<Product>>(emptyList())
    val checkoutProducts: StateFlow<List<Product>> = _checkoutProducts.asStateFlow()

    fun setCheckoutProducts(products: List<Product>) {
        _checkoutProducts.value = products
    }

    fun loadCartItems() {
        viewModelScope.launch {
            val items = useCase.getCartItems()
            _cartItems.value = items
            _totalPrice.value = items.sumOf { it.price * it.quantity }
            getTotalCart()
        }
    }

    fun addOrUpdateCartItem(product: Product) {
        viewModelScope.launch {
            useCase.addOrUpdateCartItem(product)
            loadCartItems()
        }
    }

    fun updateItemQuantity(id: Int, quantity: Int) {
        viewModelScope.launch {
            useCase.updateQuantity(id, quantity)
            loadCartItems()
        }
    }

    fun removeItem(item: Product) {
        viewModelScope.launch {
            useCase.removeItem(item)
            loadCartItems()
        }
    }

    fun getTotalCart() {
        viewModelScope.launch {
            val total = useCase.getTotalCart()
            _totalCart.value = total
        }
    }
}