package com.vikas.paging3.view.favorite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.RecyclerView
import com.vikas.paging3.R
import com.vikas.paging3.view.adapters.pagination.LoaderStateAdapter
import com.vikas.paging3.view.adapters.pagination.MoviePagingAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class FavoriteFragment :  Fragment(R.layout.fragment_favorite) {

    lateinit var favoriteRecyclerView: RecyclerView
    lateinit var favoriteViewModel: FavoriteViewModel
    lateinit var adapter: MoviePagingAdapter
    lateinit var loaderStateAdapter: LoaderStateAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMembers()
        setUpViews(view)
        fetchDoggoImages()
    }

    private fun fetchDoggoImages() {
        lifecycleScope.launch {
            favoriteViewModel.fetchDoggoImages().distinctUntilChanged().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun initMembers() {
        favoriteViewModel = ViewModelProvider(requireActivity()).get(FavoriteViewModel::class.java)
        adapter = MoviePagingAdapter{
            findNavController().navigate(
                FavoriteFragmentDirections
                    .actionFavoriteFragmentToDetailsFragment(
                        Integer.parseInt(it.id)
                    )
            )
        }
        loaderStateAdapter = LoaderStateAdapter { adapter.retry() }
    }

    private fun setUpViews(view: View) {
        favoriteRecyclerView = view.findViewById(R.id.favoriteRecyclerView)
        favoriteRecyclerView.adapter = adapter.withLoadStateFooter(
            loaderStateAdapter
        )
    }
}