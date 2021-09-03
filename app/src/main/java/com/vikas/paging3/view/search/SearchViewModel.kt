package com.vikas.paging3.view.search

import androidx.lifecycle.*
import androidx.lifecycle.map
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vikas.paging3.model.Movie
import com.vikas.paging3.repository.MovieRepository
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@HiltViewModel
class SearchViewModel
@Inject constructor(
    private val repository: MovieRepository,
     private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val KEY_MOVIE_QUERY = "query"
        const val DEFAULT_MOVIE_QUERY = ""
    }

    private val clearListCh = Channel<Unit>(Channel.CONFLATED)

    val filteredData = flowOf(

        clearListCh.receiveAsFlow().map { PagingData.empty() },
        savedStateHandle.getLiveData<String>(KEY_MOVIE_QUERY)
        .asFlow().flatMapLatest {
            repository.letSearchMovieFlowDb(it)
        }.cachedIn(viewModelScope)
    ).flattenMerge(2)

    init {
        if (!savedStateHandle.contains(KEY_MOVIE_QUERY)) {
            savedStateHandle.set(KEY_MOVIE_QUERY, DEFAULT_MOVIE_QUERY)
        }
    }

    fun getQuery() = savedStateHandle.get<String>(KEY_MOVIE_QUERY)

    fun shouldShowSubreddit(
        subreddit: String
    ) = savedStateHandle.get<String>(KEY_MOVIE_QUERY) != subreddit

    fun showSubreddit(subreddit: String) {
        if (!shouldShowSubreddit(subreddit)) return
        savedStateHandle.set(KEY_MOVIE_QUERY, subreddit)
    }
}