package com.vikas.paging3.repository.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remotekeys")
data class RemoteKeys(@PrimaryKey val repoId: String, val prevKey: Int?, val nextKey: Int?)
