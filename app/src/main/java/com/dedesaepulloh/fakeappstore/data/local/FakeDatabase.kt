package com.dedesaepulloh.fakeappstore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CartEntity::class],
    version = 3,
    exportSchema = false
)
abstract class FakeDatabase : RoomDatabase() {
    abstract fun fakeDao(): FakeDao
}