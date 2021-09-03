package com.vikas.paging3.view.details

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.vikas.paging3.model.MovieVideoResponse
import com.vikas.paging3.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class DetailsViewModel
@Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private var _movieId = MutableStateFlow(0)

    @ExperimentalCoroutinesApi
    val movie : Flow<MovieVideoResponse?> = _movieId.flatMapLatest {
        repository.letMovieTrailersFlowDb(it).onStart {

        }
    }

    fun setMovie(id: Int) {
        _movieId.value = id
    }
}