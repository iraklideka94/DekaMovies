package com.example.dekamovies.di

import android.app.Application
import androidx.room.Room
import com.example.dekamovies.movielist.data.local.movie.MovieDataBase
import com.example.dekamovies.movielist.data.remote.MovieApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()


    @Provides
    @Singleton
    fun providesMovieApi(): MovieApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(MovieApi.BASE_URL)
            .client(client)
            .build()
            .create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun providesMovieDatabase(app: Application): MovieDataBase {
        return Room.databaseBuilder(app, MovieDataBase::class.java, "movie_database")
            .fallbackToDestructiveMigration()
            .build()
    }
}