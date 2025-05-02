package com.dedesaepulloh.fakeappstore.domain.repository

import com.dedesaepulloh.fakeappstore.domain.model.Product
import com.dedesaepulloh.fakeappstore.domain.model.User
import com.dedesaepulloh.fakeappstore.utils.Result

interface FakeRepository {

    suspend fun login(email: String, password: String) : Result<String>

    suspend fun logout() : Result<String>

    suspend fun isLoggedIn(): Boolean

    suspend fun getUser(): User?

    suspend fun insert(user: User)

    suspend fun getAllProducts(): Result<List<Product>>

    suspend fun getProductDetail(id: Int): Result<Product?>

    suspend fun getCartItems(): List<Product>

    suspend fun addOrUpdateCartItem(item: Product)

    suspend fun removeItem(item: Product)

    suspend fun getTotalCart(): Int

    suspend fun updateQuantity(id: Int, quantity: Int)

    suspend fun getCategories(): Result<List<String>>

    suspend fun getProductByCategory(category: String): Result<List<Product>>

}