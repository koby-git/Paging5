package com.vikas.paging3.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.vikas.paging3.R
import com.vikas.paging3.model.Movie
import com.vikas.paging3.util.Constants.IMAGE_BASE_URL

class MovieChildAdapterDiff(
   private val onMovieClick : (movie : Movie) -> Unit,
) : RecyclerView.Adapter<MovieChildAdapterDiff.MovieListViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
           return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return  oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, diffCallback)

    var dataSet: List<Movie>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        return MovieListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.row_movie_item, parent,
                    false
                )
        )
    }

    private var onItemClickListener: ((Movie) -> Unit)? = null

    override fun getItemCount(): Int {
        return dataSet.size
    }

    class MovieListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val movieImageView: ImageView = view.findViewById(R.id.movie_image)
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {

        holder.movieImageView.load(IMAGE_BASE_URL + dataSet[position].imageUrl)

        holder.itemView.apply {
            setOnClickListener {
                onItemClickListener?.let { click ->
                    click(dataSet[position])
                }
            }
        }

        holder.itemView.apply {
            this.setOnClickListener{
                onMovieClick.invoke(dataSet[position])
            }
        }
    }

}
