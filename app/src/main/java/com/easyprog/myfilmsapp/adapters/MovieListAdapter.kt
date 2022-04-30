package com.easyprog.myfilmsapp.adapters

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.easyprog.myfilmsapp.R
import com.easyprog.myfilmsapp.adapters.interface_adapters.MovieClickHandler
import com.easyprog.myfilmsapp.databinding.MovieItemBinding
import com.easyprog.myfilmsapp.model.MovieListItemModel
import com.squareup.picasso.Picasso

class MovieListAdapter : RecyclerView.Adapter<MovieListAdapter.MovieListViewHolder>() {

    private var movieClickHandler: MovieClickHandler? = null

    private val movieList = mutableListOf<MovieListItemModel>()

//    fun getMovieList(newMovieList: List<MovieListItemModel>) {
//        movieList.clear()
//        movieList.addAll(newMovieList)
//        notifyDataSetChanged()
//    }

    fun getMovieList(newMovie: MovieListItemModel) {
        if (!movieList.contains(newMovie)) {
            movieList.add(newMovie)
            notifyItemInserted(movieList.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        return MovieListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false),
            movieClickHandler
        )
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    override fun getItemCount(): Int = movieList.size

    class MovieListViewHolder(item: View, private val movieClickHandler: MovieClickHandler?) : RecyclerView.ViewHolder(item) {
        private val binding = MovieItemBinding.bind(item)
        fun bind(model: MovieListItemModel) = with(binding) {
            movieName.text = model.name
            Picasso.get()
                .load(model.preview)
                .resize(150, bannerMovie.measuredHeight)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_insert_photo)
                .into(bannerMovie)
            materialCardMovieItem.setOnClickListener {
                movieClickHandler?.onItemClick(model.id)
            }
        }
    }

    fun attachClickHandler(movieClickHandler: MovieClickHandler) {
        this.movieClickHandler = movieClickHandler
    }
}