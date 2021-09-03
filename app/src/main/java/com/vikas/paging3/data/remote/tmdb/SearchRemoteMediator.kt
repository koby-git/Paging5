package com.vikas.paging3.data.remote.tmdb

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.vikas.paging3.data.local.MovieDatabase
import com.vikas.paging3.data.local.SearchRemoteKeys
import com.vikas.paging3.model.Movie
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class SearchRemoteMediator(
    private val theMovieDbService: TheMovieDbService,
    private val movieDatabase: MovieDatabase,
    private val query: String = "",
) : RemoteMediator<Int, Movie>() {

    private val movieDao = movieDatabase.movieDao()
    private val searchRemoteKeysDao = movieDatabase.searchRemoteKeysDao()

    override suspend fun initialize(): InitializeAction {
        // Require that remote REFRESH is launched on initial load and succeeds before launching
        // remote PREPEND / APPEND.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movie>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                if(query.isEmpty()) return MediatorResult.Success(endOfPaginationReached = true)

                val remoteKey = movieDatabase.withTransaction {
                    searchRemoteKeysDao.remoteKeysDoggoId(query)
                }

                if (remoteKey.nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                remoteKey.nextKey
            }

        }

        try {
            val response = theMovieDbService.getSearchedMovies(
                page = page,
                query = query,
            )


            val isEndOfList = response.results.isEmpty()
            movieDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    searchRemoteKeysDao.clearRemoteKeys()
                    movieDao.deleteSearchMovies()
                }

                response.results.forEach {
                    it.isSearched = true
                }

                searchRemoteKeysDao.insert(SearchRemoteKeys(repoId = query, nextKey = response.page + 1))
                movieDao.insertAll(response.results)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }
}