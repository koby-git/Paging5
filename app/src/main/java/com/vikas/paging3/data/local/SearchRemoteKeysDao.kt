package com.vikas.paging3.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: SearchRemoteKeys)

    @Query("SELECT * FROM SEARCH_REMOTE_KEYS WHERE repoId = :id")
    suspend fun remoteKeysDoggoId(id: String): SearchRemoteKeys

    @Query("DELETE FROM SEARCH_REMOTE_KEYS")
    suspend fun clearRemoteKeys()
}