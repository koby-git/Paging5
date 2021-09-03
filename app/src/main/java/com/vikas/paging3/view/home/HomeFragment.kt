package com.vikas.paging3.view.home

import android.os.Bundle
import android.util.Log.d
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.RecyclerView
import com.vikas.paging3.R
import com.vikas.paging3.util.Result
import com.vikas.paging3.view.adapters.SectionsAdapterDiff
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "HomeFragment"

@ExperimentalPagingApi
class HomeFragment : Fragment(R.layout.fragment_home) {

    lateinit var homeRecyclerView: RecyclerView
    lateinit var homeViewModel: HomeViewModel

    private val adapter = SectionsAdapterDiff({ sectionTitle ->

    },{ movie ->
        findNavController().navigate(
        HomeFragmentDirections
            .actionHomeFragmentToDetailsFragment(Integer.parseInt(movie.id))
        )
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMembers()
        setUpViews(view)
        fetchDoggoImages()
    }

    private fun fetchDoggoImages() {

        lifecycleScope.launch {

            homeViewModel.movieCategory.observe(viewLifecycleOwner, {
                when(it){
                    Result.Error -> {}
                    Result.Loading -> {}
                    is Result.Success -> {
                        adapter.submitList(it.data)
                    }
                }
            })
        }
    }

    private fun initMembers() {
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
    }

    private fun setUpViews(view: View) {
        homeRecyclerView = view.findViewById(R.id.home_recyclerview)
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        homeRecyclerView.adapter = adapter
    }
}