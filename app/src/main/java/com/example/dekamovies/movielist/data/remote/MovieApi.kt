package com.example.dekamovies.movielist.data.remote

import com.example.dekamovies.movielist.data.remote.respond.MovieListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/{category}")
    suspend fun getMovieList(
        @Path("category") category: String,
        @Query("page") page: Int,
        @Query("api_key") apikey: String = API_KEY

    ): MovieListDto


    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500/"
        const val API_KEY = "e6e8ad07e63873239199321c94b6e23a"
    }
}