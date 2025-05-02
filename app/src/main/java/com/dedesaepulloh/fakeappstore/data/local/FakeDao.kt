package com.dedesaepulloh.fakeappstore.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface FakeDao {

    @Query("SELECT * FROM cart_items")
    suspend fun getCartItems(): List<CartEntity>

    @Query("SELECT * FROM cart_items WHERE id = :itemId LIMIT 1")
    suspend fun getCartItemById(itemId: Int): CartEntity?

    @Insert
    suspend fun insertCartItem(item: CartEntity)

    @Update
    suspend fun updateCartItem(item: CartEntity)

    @Delete
    suspend fun deleteCartItem(item: CartEntity)

    @Query("SELECT SUM(quantity) FROM cart_items")
    suspend fun getTotalQuantity(): Int?

    @Query("UPDATE cart_items SET quantity = :quantity WHERE id = :itemId")
    suspend fun updateQuantity(itemId: Int, quantity: Int)

}

