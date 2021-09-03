package com.vikas.paging3.view.adapters

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vikas.paging3.R
import com.vikas.paging3.model.Movie
import com.vikas.paging3.model.MovieCategory

class SectionsAdapterDiff(
    onSectionClick : (string : String) -> Unit,
    private val onMovieClick : (movie : Movie) -> Unit,
) : ListAdapter<MovieCategory,SectionsAdapterDiff.MovieListViewHolder>(REPO_COMPARATOR) {

    val scrollStates = mutableMapOf<Int, Parcelable?>()

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<MovieCategory>() {
            override fun areItemsTheSame(oldItem: MovieCategory, newItem: MovieCategory) =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: MovieCategory, newItem: MovieCategory) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        return MovieListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.row_section_item,
                    parent,
                    false
                )
        )
    }

    override fun onViewRecycled(holder: MovieListViewHolder) {
        super.onViewRecycled(holder)

        val key = holder.layoutPosition
        scrollStates[key] = holder.sectionRecyclerView.layoutManager?.onSaveInstanceState()
    }


    class MovieListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var categoryTitle: TextView = view.findViewById(R.id.section_title)
        var sectionRecyclerView: RecyclerView = view.findViewById(R.id.section_child_recycler_view)
  }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {

        val key = holder.layoutPosition
        val state = scrollStates[key]
        if(state != null){
            holder.sectionRecyclerView.layoutManager?.onRestoreInstanceState(state)
        }else{
            holder.sectionRecyclerView.layoutManager?.scrollToPosition(0)
        }

        holder.categoryTitle.text = getItem(position).title

        val childMembersAdapter = MovieChildAdapterDiff(onMovieClick)
        childMembersAdapter.stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
        childMembersAdapter.dataSet = getItem(position).result

        holder.sectionRecyclerView.layoutManager = LinearLayoutManager(
            holder.itemView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        holder.sectionRecyclerView.adapter = childMembersAdapter
    }


}
