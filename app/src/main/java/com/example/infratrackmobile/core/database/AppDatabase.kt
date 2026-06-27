package com.example.infratrackmobile.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.infratrackmobile.core.database.entity.PlaceholderEntity

@Database(entities = [PlaceholderEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    // DAOs will be added here
}
