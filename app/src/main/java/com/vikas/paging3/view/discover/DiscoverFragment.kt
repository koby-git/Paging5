package com.vikas.paging3.view.discover

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.RecyclerView
import com.vikas.paging3.R
import com.vikas.paging3.util.Result
import com.vikas.paging3.view.adapters.pagination.LoaderStateAdapter
import com.vikas.paging3.view.adapters.pagination.MoviePagingAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class DiscoverFragment : Fragment(R.layout.fragment_discover) {

    private lateinit var discoverRecyclerView: RecyclerView
    private lateinit var discoverViewModel: DiscoverViewModel
    private lateinit var loaderStateAdapter: LoaderStateAdapter
    private lateinit var adapter: MoviePagingAdapter
    private lateinit var discoverProgressbar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loaderStateAdapter = LoaderStateAdapter { adapter.retry() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMembers()
        setUpViews(view)
        fetchDoggoImages()
    }

    private fun fetchDoggoImages() {

        discoverViewModel.discoverMovieList
            .distinctUntilChanged()
            .observe(viewLifecycleOwner, {
                discoverProgressbar.visibility = View.GONE
                adapter.submitData(lifecycle, it)
            })
    }

    private fun initMembers() {
        discoverViewModel = ViewModelProvider(requireActivity())[DiscoverViewModel::class.java]
        adapter = MoviePagingAdapter {
            findNavController().navigate(
                DiscoverFragmentDirections
                    .actionDiscoverFragmentToDetailsFragment(
                        Integer.parseInt(it.id)
                    )
            )
        }
    }

    private fun setUpViews(view: View) {
        discoverRecyclerView = view.findViewById(R.id.rvDoggoRoom)
        discoverProgressbar = view.findViewById(R.id.discover_progressbar)
        discoverRecyclerView.adapter = adapter.withLoadStateFooter(
            loaderStateAdapter
        )

    }
}