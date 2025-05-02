package com.dedesaepulloh.fakeappstore.domain

import com.dedesaepulloh.fakeappstore.domain.model.Product
import com.dedesaepulloh.fakeappstore.domain.model.User
import com.dedesaepulloh.fakeappstore.domain.repository.FakeRepository
import com.dedesaepulloh.fakeappstore.utils.Result
import javax.inject.Inject

class FakeUseCase @Inject constructor(
    private val repository: FakeRepository
) {
    suspend fun login(email: String, password: String): Result<String> = repository.login(email, password)

    suspend fun logout(): Result<String> = repository.logout()

    suspend fun isLoggedIn() = repository.isLoggedIn()

    suspend fun getUser() = repository.getUser()

    suspend fun insert(user: User) = repository.insert(user)

    suspend fun getAllProducts() = repository.getAllProducts()

    suspend fun getProductDetail(id: Int) = repository.getProductDetail(id)

    suspend fun getCartItems() = repository.getCartItems()

    suspend fun addOrUpdateCartItem(item: Product) = repository.addOrUpdateCartItem(item)

    suspend fun removeItem(item: Product) = repository.removeItem(item)

    suspend fun getTotalCart(): Int = repository.getTotalCart()

    suspend fun updateQuantity(id: Int, quantity: Int) = repository.updateQuantity(id, quantity)

    suspend fun getCategories() = repository.getCategories()

    suspend fun getProductByCategory(category: String) = repository.getProductByCategory(category)

}