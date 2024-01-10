package com.example.dekamovies.movielist.domain.repository

import com.example.dekamovies.movielist.domain.model.Movie
import com.example.dekamovies.movielist.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieListRepository {

    suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>>


    suspend fun getMovie(id: Int): Flow<Resource<Movie>>
}