package com.vikas.paging3.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.room.Room
import com.vikas.paging3.Constants.API_ENDPOINT
import com.vikas.paging3.Constants.API_KEY
import com.vikas.paging3.Constants.HEADER_API_KEY
import com.vikas.paging3.Constants.MOVIE_DB
import com.vikas.paging3.data.local.MovieDatabase
import com.vikas.paging3.data.remote.TheMovieDbService
import com.vikas.paging3.repository.MovieRepository
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
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@ExperimentalPagingApi
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMovieDatabase(
        @ApplicationContext context: Context
    ) = Room
        .databaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java,
            MOVIE_DB
        ).build()

    @Singleton
    @Provides
    fun provideTheMovieDbService(retrofit: Retrofit): TheMovieDbService {
        return retrofit.create(TheMovieDbService::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @ExperimentalPagingApi
    @Singleton
    @Provides
    fun provideOkHttpNetworkInterceptor(): Interceptor {
        return Interceptor { chain ->
            val newRequest =
                chain.request().newBuilder().addHeader(HEADER_API_KEY, API_KEY).build()
            chain.proceed(newRequest)
        }
    }

    @ExperimentalPagingApi
    @Singleton
    @Provides
    fun provideHttpLogger(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @ExperimentalPagingApi
    @Singleton
    @Provides
    fun provideOkHttpClient(
        okHttpLogger: HttpLoggingInterceptor,
        okHttpNetworkInterceptor: Interceptor
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
        doggoApiService: TheMovieDbService,
        appDatabase: MovieDatabase
    ) = MovieRepository(doggoApiService,appDatabase)

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

}
