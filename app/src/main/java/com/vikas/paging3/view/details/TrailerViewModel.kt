package com.vikas.paging3.view.details

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.vikas.paging3.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class TrailerViewModel
@Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {



}