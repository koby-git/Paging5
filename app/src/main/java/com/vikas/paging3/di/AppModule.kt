package com.vikas.paging3.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.room.Room
import com.vikas.paging3.util.Constants.TMDB_API_ENDPOINT
import com.vikas.paging3.util.Constants.MOVIE_DB
import com.vikas.paging3.data.local.MovieDatabase
import com.vikas.paging3.data.remote.imdb.ImdbService
import com.vikas.paging3.data.remote.tmdb.TheMovieDbService
import com.vikas.paging3.repository.MovieRepository
import com.vikas.paging3.util.Constants.IMDB_BASE_URL
import com.vikas.paging3.util.Constants.IMDB_RETROFIT
import com.vikas.paging3.util.Constants.IMDB_SERVICE
import com.vikas.paging3.util.Constants.TMDB_API_KEY
import com.vikas.paging3.util.Constants.TMDB_RETROFIT
import com.vikas.paging3.util.Constants.TMDB_SERVICE
import com.vikas.paging3.util.ISOListCodes
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
import retrofit2.http.Query
import javax.inject.Named
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
        )
        .fallbackToDestructiveMigration()
        .build()

    @Named(TMDB_SERVICE)
    @Singleton
    @Provides
    fun provideTheMovieDbService(
        @Named(TMDB_RETROFIT)
        retrofit: Retrofit): TheMovieDbService {
        return retrofit.create(TheMovieDbService::class.java)
    }

    @Named(TMDB_RETROFIT)
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(TMDB_API_ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }


    @Named(IMDB_SERVICE)
    @Singleton
    @Provides
    fun provideImdbService(
        @Named(IMDB_RETROFIT)
        retrofit: Retrofit
    ): ImdbService {
        return retrofit.create(ImdbService::class.java)
    }

    @Named(IMDB_RETROFIT)
    @Singleton
    @Provides
    fun provideImdbRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(IMDB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @ExperimentalPagingApi
    @Singleton
    @Provides
    fun provideOkHttpNetworkInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request();
            val url = request.url.newBuilder()
                .addQueryParameter("api_key", TMDB_API_KEY)
                .addQueryParameter("language", ISOListCodes.getLocaleLanguage())
            .build();
            val newRequest = request.newBuilder().url(url).build();
            chain.proceed(newRequest)
        }
    }

/*
    @ExperimentalPagingApi
    @Singleton
    @Provides
    fun provideOkHttpNetworkInterceptor(): Interceptor {
        return Interceptor { chain ->
            val newRequest =
                chain.request().newBuilder().addHeader(HEADER_API_KEY, API_KEY).build()
            chain.proceed(newRequest)
        }
    }*/

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
        @Named(TMDB_SERVICE)
        doggoApiService: TheMovieDbService,
        @Named(IMDB_SERVICE)
        imdbService: ImdbService,
        appDatabase: MovieDatabase,
        @ApplicationContext context: Context
    ) = MovieRepository(
        doggoApiService,
        imdbService,
        appDatabase,
        context
    )

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

}
