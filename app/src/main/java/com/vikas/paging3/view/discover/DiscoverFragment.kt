package com.vikas.paging3.view.discover

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.RecyclerView
import com.vikas.paging3.R
import com.vikas.paging3.view.discover.adapter.MoviePagingAdapter
import com.vikas.paging3.view.discover.adapter.LoaderStateAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class DiscoverFragment : Fragment(R.layout.fragment_room) {

    lateinit var rvDoggoRoom: RecyclerView
    lateinit var roomViewModel: DiscoverViewModel
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
            roomViewModel.fetchDoggoImages().distinctUntilChanged().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun initMembers() {
        roomViewModel = ViewModelProvider(requireActivity()).get(DiscoverViewModel::class.java)
        adapter = MoviePagingAdapter()
        loaderStateAdapter = LoaderStateAdapter { adapter.retry() }
    }

    private fun setUpViews(view: View) {
        rvDoggoRoom = view.findViewById(R.id.rvDoggoRoom)
        rvDoggoRoom.adapter = adapter.withLoadStateFooter(
            loaderStateAdapter
        )
    }
}