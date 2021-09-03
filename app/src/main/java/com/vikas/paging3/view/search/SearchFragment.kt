package com.vikas.paging3.view.search

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log.d
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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
import java.util.*
import android.os.Parcelable
import androidx.paging.LoadState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter

private val TAG = "SearchFragment"

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var searchRecyclerView: RecyclerView

    private lateinit var searchViewModel: SearchViewModel
    lateinit var adapter: MoviePagingAdapter
    private lateinit var loaderStateAdapter: LoaderStateAdapter
    private lateinit var searchRepo : EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMembers()
        setUpViews(view)

        searchRepo.setText(searchViewModel.getQuery())

        searchRepo.setOnEditorActionListener { tv, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput()
                hideKeyboard()
                true
            } else {
                false
            }
        }
        searchRepo.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput()
                hideKeyboard()
                true
            } else {
                false
            }
        }

        lifecycleScope.launch {
            searchViewModel.filteredData.distinctUntilChanged()
                .collectLatest {
                    adapter.submitData(it)
                }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow
                // Only emit when REFRESH changes, as we only want to react on loads replacing the
                // list.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                // Scroll to top is synchronous with UI updates, even if remote load was triggered.

        }

    }

    private fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun updateRepoListFromInput() {
        searchRepo.text.trim().toString().let {
            if (it.isNotBlank() && searchViewModel.shouldShowSubreddit(it)) {
                searchViewModel.showSubreddit(it)
            }
        }


//        searchViewModel.fetchDoggoImages(searchRepo.text.toString())
    }

    private fun initMembers() {
        searchViewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)
        adapter = MoviePagingAdapter {
            findNavController().navigate(
                SearchFragmentDirections
                    .actionSearchFragmentToDetailsFragment(
                        Integer.parseInt(it.id)
                    )
            )
        }
        loaderStateAdapter = LoaderStateAdapter { adapter.retry() }
    }

    private fun setUpViews(view: View) {
        searchRecyclerView = view.findViewById(R.id.searchRecyclerView)
        searchRepo = view.findViewById(R.id.search_repo)
        searchRecyclerView.adapter = adapter.withLoadStateFooter(
            loaderStateAdapter
        )

    }


}