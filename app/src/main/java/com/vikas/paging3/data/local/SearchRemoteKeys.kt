package com.vikas.paging3.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vikas.paging3.util.Constants.SEARCH_REMOTE_KEYS_TABLE

@Entity(tableName = SEARCH_REMOTE_KEYS_TABLE)
data class SearchRemoteKeys(@PrimaryKey val repoId: String, val nextKey: Int?)