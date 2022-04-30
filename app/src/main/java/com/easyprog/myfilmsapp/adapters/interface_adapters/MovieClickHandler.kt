package com.easyprog.myfilmsapp.adapters.interface_adapters

import com.easyprog.myfilmsapp.model.MovieListItemModel

interface MovieClickHandler {
    fun onItemClick(movieId: Int)
}