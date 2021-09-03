package com.vikas.paging3.view.adapters.pagination

import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vikas.paging3.R

class LoaderStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoaderStateAdapter.LoaderViewHolder>() {

    override fun onBindViewHolder(holder: LoaderViewHolder, loadState: LoadState) {
        holder.bindState(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoaderViewHolder {
        return LoaderViewHolder.getInstance(parent, retry)
    }

    /**
     * view holder class for footer loader and error state handling
     */
    class LoaderViewHolder(view: View, retry: () -> Unit) : RecyclerView.ViewHolder(view) {

        companion object {
            fun getInstance(parent: ViewGroup, retry: () -> Unit): LoaderViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.item_paging_loader, parent, false)
                return LoaderViewHolder(view, retry)
            }
        }

        private val errorMessage: TextView = view.findViewById(R.id.error_msg)
        private val progressBar: ProgressBar = view.findViewById(R.id.progress_bar)
        private val retryButton: Button = view.findViewById(R.id.retry_button)

        init {
            retryButton.setOnClickListener {
                retry.invoke()
            }
        }

        fun bindState(loadState: LoadState) {
            d("BindState","LoadingState")
            progressBar.isVisible = loadState is LoadState.Loading
            retryButton.isVisible = loadState is LoadState.Error
            errorMessage.isVisible = loadState is LoadState.Error

            if (loadState is LoadState.Error) {
                errorMessage.text = loadState.error.localizedMessage
            }
        }
    }
}