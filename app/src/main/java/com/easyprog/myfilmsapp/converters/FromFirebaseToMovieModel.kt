package com.easyprog.myfilmsapp.converters

import com.easyprog.myfilmsapp.model.MovieModel
import com.google.firebase.database.DataSnapshot

fun DataSnapshot.getFilmModel(): MovieModel = this.getValue(MovieModel::class.java) ?: MovieModel()