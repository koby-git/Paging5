package com.vikas.paging3.view.room

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vikas.paging3.R
import com.vikas.paging3.view.room.adapter.LoaderDoggoImageAdapter
import com.vikas.paging3.view.room.adapter.LoaderStateAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class RoomFragment : Fragment(R.layout.fragment_room) {

    lateinit var rvDoggoRoom: RecyclerView
    lateinit var roomViewModel: RoomViewModel
    lateinit var adapter: LoaderDoggoImageAdapter
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
        roomViewModel = ViewModelProvider(requireActivity()).get(RoomViewModel::class.java)
        adapter = LoaderDoggoImageAdapter()
        loaderStateAdapter = LoaderStateAdapter { adapter.retry() }
    }

    private fun setUpViews(view: View) {
        rvDoggoRoom = view.findViewById(R.id.rvDoggoRoom)
        rvDoggoRoom.adapter = adapter.withLoadStateHeaderAndFooter(
            header = loaderStateAdapter,
            footer = loaderStateAdapter)
    }
}