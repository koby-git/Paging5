package com.vikas.paging3.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.vikas.paging3.Constants.DEFAULT_PAGE_INDEX
import com.vikas.paging3.data.local.MovieDatabase
import com.vikas.paging3.data.local.RemoteKeys
import com.vikas.paging3.data.remote.TheMovieDbService
import com.vikas.paging3.model.Movie
import retrofit2.HttpException
import java.io.IOException


@ExperimentalPagingApi
class MovieMediator(
    private val theMovieDbService: TheMovieDbService,
    private val movieDatabase: MovieDatabase
    ) : RemoteMediator<Int, Movie>() {

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, Movie>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
            }
            LoadType.PREPEND -> {

                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKey
            }
            LoadType.APPEND -> {

                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }

        try {
//            val response = doggoApiService.getDoggoImages(page, state.config.pageSize)
            val response = theMovieDbService.getDiscoverMoviesList(page = page).results
            val isEndOfList = response.isEmpty()
            movieDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    movieDatabase.remoteKeysDao().clearRemoteKeys()
                    movieDatabase.movieDao().deleteAllMovies()
                }
                val prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfList) null else page + 1
                val keys = response.map {
                    RemoteKeys(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                movieDatabase.remoteKeysDao().insertAll(keys)
                movieDatabase.movieDao().insertAll(response)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    /**
     * this returns the page key or the final end of list success result
     */
    /*
        suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, DoggoImageModel>): Any? {
            return when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getClosestRemoteKey(state)
                    remoteKeys?.nextKey?.minus(1) ?: DEFAULT_PAGE_INDEX
                }
                LoadType.APPEND -> {
                    val remoteKeys = getLastRemoteKey(state)
                        ?: throw InvalidObjectException("Remote key should not be null for $loadType")
                    remoteKeys.nextKey
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getFirstRemoteKey(state)
                        ?: throw InvalidObjectException("Invalid state, key should not be null")
                    //end of list condition reached
                    remoteKeys.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                    remoteKeys.prevKey
                }
            }
        }
    */

    /**
     * get the last remote key inserted which had the data
     */
    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Movie>): RemoteKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { doggo -> movieDatabase.remoteKeysDao().remoteKeysDoggoId(doggo.id) }
    }

    /**
     * get the first remote key inserted which had the data
     */
    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Movie>): RemoteKeys? {
        return state.pages
            .firstOrNull() { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { doggo -> movieDatabase.remoteKeysDao().remoteKeysDoggoId(doggo.id) }
    }

    /**
     * get the closest remote key inserted which had the data
     */
    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Movie>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                movieDatabase.remoteKeysDao().remoteKeysDoggoId(repoId)
            }
        }
    }

}