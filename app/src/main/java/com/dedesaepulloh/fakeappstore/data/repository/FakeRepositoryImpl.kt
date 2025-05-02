package com.dedesaepulloh.fakeappstore.data.repository

import com.dedesaepulloh.fakeappstore.data.local.FakeDao
import com.dedesaepulloh.fakeappstore.data.local.datastore.UserPreferences
import com.dedesaepulloh.fakeappstore.data.remote.FakeApi
import com.dedesaepulloh.fakeappstore.domain.model.Product
import com.dedesaepulloh.fakeappstore.domain.model.User
import com.dedesaepulloh.fakeappstore.domain.model.toDomain
import com.dedesaepulloh.fakeappstore.domain.model.toEntity
import com.dedesaepulloh.fakeappstore.domain.repository.FakeRepository
import com.dedesaepulloh.fakeappstore.utils.Result
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class FakeRepositoryImpl @Inject constructor(
    private val fakeApi: FakeApi,
    private val fakeDao: FakeDao,
    private val userPreferences: UserPreferences
) : FakeRepository {

    override suspend fun login(email: String, password: String): Result<String> {

        val savedUser = userPreferences.readUser().first()
        return if (savedUser != null &&
            savedUser.email == email &&
            savedUser.password == password
        ) {
            userPreferences.setLoginStatus(true)
            Result.Success("Login Successful")
        } else {
            Result.Error("Please check your email and password again.")
        }
    }

    override suspend fun logout(): Result<String> {
        val savedUser = userPreferences.readUser().first()
        return if (savedUser != null) {
            userPreferences.setLoginStatus(false)
            Result.Success("Logout Successful")
        } else {
            Result.Error("User not found")
        }
    }

    override suspend fun isLoggedIn(): Boolean = userPreferences.getLoginStatus().firstOrNull() ?: false

    override suspend fun getUser(): User? {
        return userPreferences.readUser().first()
    }

    override suspend fun insert(user: User) {
        userPreferences.save(user)
    }

    override suspend fun getAllProducts(): Result<List<Product>> {
        return try {
            val response = fakeApi.getProducts()
            if (response.isSuccessful) {
                val data = response.body()?.map { it.toDomain() }
                Result.Success(data ?: emptyList())
            } else {
                Result.Error("Failed to fetch products")
            }
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    override suspend fun getProductDetail(id: Int): Result<Product?> {
        return try {
            val response = fakeApi.getProductDetail(id)
            if (response.isSuccessful) {
                val data = response.body()?.toDomain()
                Result.Success(data)
            } else {
                Result.Error("Failed to fetch products")
            }
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    override suspend fun getCartItems(): List<Product> {
        return fakeDao.getCartItems().map { it.toDomain() }
    }

    override suspend fun addOrUpdateCartItem(item: Product) {
        val existing = fakeDao.getCartItemById(item.id)
        if (existing != null) {
            val updated = existing.copy(quantity = existing.quantity + item.quantity)
            fakeDao.updateCartItem(updated)
        } else {
            fakeDao.insertCartItem(item.toEntity())
        }
    }

    override suspend fun removeItem(item: Product) = fakeDao.deleteCartItem(item.toEntity())

    override suspend fun getTotalCart(): Int = fakeDao.getCartItems().size

    override suspend fun updateQuantity(id: Int, quantity: Int) =
        fakeDao.updateQuantity(id, quantity)

    override suspend fun getCategories(): Result<List<String>> {
        return try {
            val response = fakeApi.getCategories()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.Success(it)
                } ?: Result.Error("Empty response body")
            } else {
                Result.Error("Error: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.localizedMessage}")
        }
    }

    override suspend fun getProductByCategory(category: String): Result<List<Product>> {
        return try {
            val response = fakeApi.getProductByCategory(category)
            if (response.isSuccessful) {
                val data = response.body()?.map { it.toDomain() }
                Result.Success(data ?: emptyList())
            } else {
                Result.Error("Failed to fetch products")
            }
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "Unknown error")
        }
    }
}