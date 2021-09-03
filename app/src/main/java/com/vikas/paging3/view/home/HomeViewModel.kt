package com.vikas.paging3.view.home
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.paging.ExperimentalPagingApi
import com.vikas.paging3.model.MovieCategory
import com.vikas.paging3.repository.MovieRepository
import com.vikas.paging3.util.Resource
import com.vikas.paging3.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject
@ExperimentalPagingApi
@HiltViewModel
class HomeViewModel
@Inject constructor(
    repository: MovieRepository
) : ViewModel() {

    private val _movieCategory = repository
        .letCategorisedMovieFlowDb()
        .distinctUntilChanged()
        .asLiveData()

    val movieCategory: LiveData<Result<List<MovieCategory>>>
        get() = _movieCategory

}


