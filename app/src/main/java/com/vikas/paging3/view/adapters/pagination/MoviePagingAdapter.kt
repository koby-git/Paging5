package com.vikas.paging3.view.adapters.pagination

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.vikas.paging3.util.Constants.IMAGE_BASE_URL
import com.vikas.paging3.R
import com.vikas.paging3.model.Movie
import com.vikas.paging3.view.adapters.pagination.MoviePagingAdapter.*

class MoviePagingAdapter(
    private val onMovieClick : (movie : Movie) -> Unit,
) : PagingDataAdapter<Movie, MovieViewHolder>(MOVIE_COMPARATOR) {

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie) =
                oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.row_movie_item,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.movieImage.load(
            IMAGE_BASE_URL + getItem(position)?.imageUrl
        ) {
            placeholder(R.drawable.ic_baseline_local_movies_24)
        }

        holder.itemView.setOnClickListener {
            getItem(position)?.let {
                    movie -> onMovieClick.invoke(movie)
            }
        }
    }

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var movieImage: ImageView = view.findViewById(R.id.movie_image)
    }

}