package com.vikas.paging3.repository

import android.content.Context
import android.util.Log.d
import androidx.paging.*
import com.vikas.paging3.R
import com.vikas.paging3.util.Constants.DEFAULT_PAGE_SIZE
import com.vikas.paging3.data.local.MovieDatabase
import com.vikas.paging3.data.remote.imdb.ImdbService
import com.vikas.paging3.data.remote.tmdb.MovieMediator
import com.vikas.paging3.data.remote.tmdb.SearchRemoteMediator
import com.vikas.paging3.data.remote.tmdb.TheMovieDbService
import com.vikas.paging3.model.Movie
import com.vikas.paging3.model.MovieCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.vikas.paging3.util.Result
import kotlin.random.Random

/**
 * repository class to manage the data flow and map it if needed
 */

const val TAG = "MovieRepository"

@ExperimentalPagingApi
class MovieRepository
    @Inject constructor(
        private val theMovieDbService: TheMovieDbService,
        private val imdbService: ImdbService,
        private val appDatabase: MovieDatabase,
        private val context : Context
) {

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = true)
    }

    fun letDiscoverMoveListFlowDb(): Flow<PagingData<Movie>> {

        val pagingSourceFactory = { appDatabase.movieDao().getDiscoverMovieList() }
        return Pager(
            config = getDefaultPageConfig(),
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = MovieMediator(
                theMovieDbService,
                appDatabase,
            )
        ).flow
    }

    fun letPopularMovieFlowDb(): Flow<PagingData<Movie>> {

        val pagingSourceFactory = { appDatabase.movieDao().getPopularMovieList() }
        return Pager(
            config = getDefaultPageConfig(),
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = MovieMediator(
                theMovieDbService,
                appDatabase,
            )
        ).flow
    }

    fun letSearchMovieFlowDb(query: String): Flow<PagingData<Movie>> {

        val pagingSourceFactory = { appDatabase.movieDao().getSearchedMovieList() }
        return Pager(
            config = getDefaultPageConfig(),
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = SearchRemoteMediator(
                theMovieDbService,
                appDatabase,
                query
            )
        ).flow
    }

    fun letCategorisedMovieFlowDb() = flow<Result<List<MovieCategory>>> {
        emit(Result.Loading)
        val categoryMovieList = ArrayList<MovieCategory>()

        categoryMovieList.add(
            MovieCategory(context.getString(R.string.now_in_cinemas)).also {
                it.result.addAll(theMovieDbService.getNowPlayingMovieListMovies(page = 1).results.toList())
            })

        categoryMovieList.add(
            MovieCategory(context.getString(R.string.trending)).also {
                it.result.addAll(theMovieDbService.getTrendingMovies().results.toList())
            })

        categoryMovieList.add(
            MovieCategory(context.getString(R.string.popular)).also {
                it.result.addAll(theMovieDbService.getPopularMoviesList(page = 1).results.toList())
            })

        categoryMovieList.add(
            MovieCategory(context.getString(R.string.top_rated)).also {
                it.result.addAll(theMovieDbService.getTopRatedMovies(page = 1).results.toList())
            })

        if (categoryMovieList.isEmpty()) {
            d(TAG, "Finish Failed")
            emit(Result.Error)
        } else {
            d(TAG, "Finish Successfully")

        }
        emit(Result.Success(categoryMovieList))

    }

    fun letMovieTrailersFlowDb(movieId: Int) = flow {
        if (movieId != 0) {
            val res = theMovieDbService.getMovieDetailsAndTrailers(movieId = movieId)
            val res2 = imdbService.getImdbRating(movieId = res.imdbId)
            res.imdbRating = res2.rating
            d(TAG,"IMDB RATING = ${res.imdbRating}")
            emit(res)
        } else {
            emit(null)
        }
    }

    fun letDailyTrendMovieFlowDb() = flow {

        val res = theMovieDbService.getDailyTrendingMovies()
        emit(res.results[Random.nextInt(0, 19)])
//         val video = res.results.map {
//             it.video
//         }
//
//        if (video.isNotEmpty()){
//            res.results.forEach{
//                if (it.video) emit(it)
//            }
//        }else(emit(res.results[0]))
    }

}