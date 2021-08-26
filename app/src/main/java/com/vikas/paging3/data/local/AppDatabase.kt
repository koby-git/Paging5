package com.vikas.paging3.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vikas.paging3.model.DoggoImageModel


@Database(version = 1, entities = [DoggoImageModel::class, RemoteKeys::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getRepoDao(): RemoteKeysDao
    abstract fun getDoggoImageModelDao(): DoggoImageModelDao
}
