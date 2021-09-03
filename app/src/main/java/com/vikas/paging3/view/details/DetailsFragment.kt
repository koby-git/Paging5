package com.vikas.paging3.view.details

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import coil.api.load
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.vikas.paging3.R
import com.vikas.paging3.util.BundleConstants.MOVIE_ID
import com.vikas.paging3.util.BundleConstants.MOVIE_KEY
import com.vikas.paging3.util.Constants.IMAGE_BASE_URL
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "DetailsFragment"

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var collapsingToolbarLayout : CollapsingToolbarLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar_layout)
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT)
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE)
        initMembers()

        val movieId = arguments?.getInt(MOVIE_ID)

        movieId?.let {
            detailsViewModel.setMovie(movieId)
        }

        val movieImage = view.findViewById<ImageView>(R.id.details_movie_image)
        val movieDesc = view.findViewById<TextView>(R.id.details_movie_description)
        val movieRating = view.findViewById<TextView>(R.id.details_movie_rating)
        val movieGenre = view.findViewById<TextView>(R.id.details_movie_genre)
        val playTrailer = view.findViewById<ImageView>(R.id.details_movie_play_trailer)
        val progressBar = view.findViewById<ProgressBar>(R.id.details_move_progressbar)

        playTrailer.setOnClickListener {
            lifecycleScope.launch {
                detailsViewModel.movie.collectLatest {
                    it?.let { movieResponse ->
                        startActivity(Intent(requireContext(), TrailerActivity::class.java).also { intent->
                            if (movieResponse.videos.results.isNotEmpty()) {
                                intent.putExtra(MOVIE_KEY, movieResponse.videos.results[0].key)
                            }
                        })
                    }
                }
            }
        }

        lifecycleScope.launch {
            detailsViewModel.movie.onStart {
                progressBar.visibility = View.VISIBLE
            }.collectLatest { movie ->
                progressBar.visibility = View.GONE
                Log.d(TAG, "detailsViewModel.movie.collectLatest()-> ")
                movie?.let {

                    movieImage.load(IMAGE_BASE_URL + it.imageUrl) {
                        placeholder(R.drawable.ic_baseline_local_movies_24)
                    }

                    if (movie.videos.results.isNotEmpty())
                        playTrailer.visibility = View.VISIBLE

                    collapsingToolbarLayout.title = it.title

                    val genresString = StringJoiner(",")
                    it.genres.forEach { gen ->
                        genresString.add(gen.name)
                    }
                    movieDesc.text = it.description
                    movieGenre.text = String.format(getString(R.string.genre),genresString.toString())
                    val imdbRating = if(it.imdbRating != null){
                        it.imdbRating
                    }else{
                        getString(R.string.no_rating)
                    }
                    movieRating.text = String.format(getString(R.string.rating),imdbRating)

                }
            }
        }



    }

    private fun initMembers() {
        detailsViewModel = ViewModelProvider(requireActivity())[DetailsViewModel::class.java]
    }

}