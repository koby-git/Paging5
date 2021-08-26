package com.vikas.paging3.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.room.Room
import com.vikas.paging3.Constants.API_ENDPOINT
import com.vikas.paging3.Constants.API_KEY
import com.vikas.paging3.Constants.DOGGO_DB
import com.vikas.paging3.Constants.HEADER_API_KEY
import com.vikas.paging3.data.local.AppDatabase
import com.vikas.paging3.data.remote.DoggoApiService
import com.vikas.paging3.repository.DoggoImagesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@ExperimentalPagingApi
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ) = Room
        .databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DOGGO_DB
        ).build()

    @Singleton
    @Provides
    fun injectDoggoApiService(retrofit: Retrofit): DoggoApiService {
        return retrofit.create(DoggoApiService::class.java)
    }

    @Singleton
    @Provides
    fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_ENDPOINT)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @ExperimentalPagingApi
    @Singleton
    @Provides
    fun getOkHttpNetworkInterceptor(): Interceptor {
        return Interceptor { chain ->
            val newRequest =
                chain.request().newBuilder().addHeader(HEADER_API_KEY, API_KEY).build()
            chain.proceed(newRequest)
        }
    }

    @ExperimentalPagingApi
    @Singleton
    @Provides
    fun getHttpLogger(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @ExperimentalPagingApi
    @Singleton
    @Provides
    fun getOkHttpClient(
        okHttpLogger: HttpLoggingInterceptor = getHttpLogger(),
        okHttpNetworkInterceptor: Interceptor = getOkHttpNetworkInterceptor()
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(okHttpLogger)
            .addInterceptor(okHttpNetworkInterceptor)
            .build()
    }


    @ExperimentalPagingApi
    @Singleton
    @Provides
    fun provideDoggoImagesRepository(
        doggoApiService: DoggoApiService,
        appDatabase: AppDatabase
    ) = DoggoImagesRepository(doggoApiService,appDatabase)

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

}
