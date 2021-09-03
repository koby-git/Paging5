package com.vikas.paging3.view.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vikas.paging3.model.Movie
import com.vikas.paging3.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import com.vikas.paging3.util.Result
import kotlinx.coroutines.flow.cache
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest

@ExperimentalPagingApi
@HiltViewModel
class DiscoverViewModel
@Inject constructor(
    repository: MovieRepository
) : ViewModel() {

    private val _discoverMovieList = repository
        .letDiscoverMoveListFlowDb()
        .distinctUntilChanged()
        .cachedIn(viewModelScope)
        .asLiveData()

    val discoverMovieList: LiveData<PagingData<Movie>>
        get() = _discoverMovieList

}