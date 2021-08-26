package com.vikas.paging3.view.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vikas.paging3.model.Movie
import com.vikas.paging3.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class DiscoverViewModel
@Inject constructor(
    private val repository: MovieRepository
) :
    ViewModel() {

    fun fetchDoggoImages(): Flow<PagingData<Movie>> {
        return repository.letDoggoImagesFlowDb().cachedIn(viewModelScope)
    }

}