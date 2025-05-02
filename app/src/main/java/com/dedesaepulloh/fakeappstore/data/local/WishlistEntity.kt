package com.dedesaepulloh.fakeappstore.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist")
data class WishlistEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val image: String,
    val price: Double
)