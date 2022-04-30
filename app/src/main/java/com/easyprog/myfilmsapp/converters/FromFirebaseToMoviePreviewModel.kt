package com.easyprog.myfilmsapp.converters

import com.easyprog.myfilmsapp.model.MovieListItemModel
import com.easyprog.myfilmsapp.model.MovieModel
import com.google.firebase.database.DataSnapshot

fun DataSnapshot.getMoviePreviewModel(): MovieListItemModel = this.getValue(MovieListItemModel::class.java) ?: MovieListItemModel()