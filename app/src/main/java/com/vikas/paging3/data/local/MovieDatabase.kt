package com.vikas.paging3.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vikas.paging3.model.Movie

@Database(entities = [Movie::class,RemoteKeys::class,SearchRemoteKeys::class], version = 6)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun remoteKeysDao():RemoteKeysDao
    abstract fun searchRemoteKeysDao():SearchRemoteKeysDao
}

