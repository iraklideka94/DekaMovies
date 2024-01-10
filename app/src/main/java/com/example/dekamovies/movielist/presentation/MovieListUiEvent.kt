package com.example.dekamovies.movielist.presentation

sealed class MovieListUiEvent {
    data class Paginate(val category: String) : MovieListUiEvent()
    object Navigate: MovieListUiEvent()
}