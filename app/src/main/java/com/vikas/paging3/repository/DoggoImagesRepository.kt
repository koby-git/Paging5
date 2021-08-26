package com.vikas.paging3.repository

import androidx.paging.*
import com.vikas.paging3.Constants.DEFAULT_PAGE_SIZE
import com.vikas.paging3.model.DoggoImageModel
import com.vikas.paging3.data.local.AppDatabase
import com.vikas.paging3.data.remote.DoggoApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * repository class to manage the data flow and map it if needed
 */
@ExperimentalPagingApi
class DoggoImagesRepository
    @Inject constructor(
    val doggoApiService: DoggoApiService,
    val appDatabase: AppDatabase
) {


    /**
     * calling the paging source to give results from api calls
     * and returning the results in the form of flow [Flow<PagingData<DoggoImageModel>>]
     * since the [PagingDataAdapter] accepts the [PagingData] as the source in later stage
     */
/*
    fun letDoggoImagesFlow(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<DoggoImageModel>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { DoggoImagePagingSource(doggoApiService) }
        ).flow
    }

    //for rxjava users
    fun letDoggoImagesObservable(pagingConfig: PagingConfig = getDefaultPageConfig()): Observable<PagingData<DoggoImageModel>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { DoggoImagePagingSource(doggoApiService) }
        ).observable
    }

    //for live data users
    fun letDoggoImagesLiveData(pagingConfig: PagingConfig = getDefaultPageConfig()): LiveData<PagingData<DoggoImageModel>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { DoggoImagePagingSource(doggoApiService) }
        ).liveData
    }
*/

    /**
     * let's define page size, page size is the only required param, rest is optional
     */
    fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = true)
    }

    fun letDoggoImagesFlowDb(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<DoggoImageModel>> {
        if (appDatabase == null) throw IllegalStateException("Database is not initialized")

        val pagingSourceFactory = { appDatabase.getDoggoImageModelDao().getAllDoggoModel() }
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = DoggoMediator(doggoApiService, appDatabase)
        ).flow
    }

}