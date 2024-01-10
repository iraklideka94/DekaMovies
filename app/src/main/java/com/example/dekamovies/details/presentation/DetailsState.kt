package com.example.dekamovies.details.presentation

import com.example.dekamovies.movielist.domain.model.Movie

data class DetailsState(
    val isLoading: Boolean = false,
    val movie: Movie? = null
)
