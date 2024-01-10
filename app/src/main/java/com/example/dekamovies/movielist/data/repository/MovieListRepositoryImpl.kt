package com.example.dekamovies.movielist.data.repository

import com.example.dekamovies.movielist.data.local.movie.MovieDataBase
import com.example.dekamovies.movielist.data.mappers.toMovie
import com.example.dekamovies.movielist.data.mappers.toMovieEntity
import com.example.dekamovies.movielist.data.remote.MovieApi
import com.example.dekamovies.movielist.domain.model.Movie
import com.example.dekamovies.movielist.domain.repository.MovieListRepository
import com.example.dekamovies.movielist.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDataBase: MovieDataBase
) : MovieListRepository {
    override suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))

            val localMovieList = movieDataBase.movieDao.getMovieListByCategory(category)

            val shouldLoadLocalMovie = localMovieList.isNotEmpty() && !forceFetchFromRemote

            if (shouldLoadLocalMovie) {
                emit(Resource.Success(
                    data = localMovieList.map { movieEntity ->
                        movieEntity.toMovie(category)
                    }
                ))

                emit(Resource.Loading(false))
                return@flow
            }

            val movieListFromApi = try {
                movieApi.getMovieList(category, page)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "An error occurred"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "An error occurred"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "An error occurred"))
                return@flow
            }

            val movieEntities = movieListFromApi.results.let {
                it.map { movieDto ->
                    movieDto.toMovieEntity(category)
                }
            }

            movieDataBase.movieDao.upsertMovieList(movieEntities)

            emit(
                Resource.Success(
                    data = movieEntities.map { movieEntity ->
                        movieEntity.toMovie(category)
                    }
                )
            )

            emit(Resource.Loading(false))


        }
    }

    override suspend fun getMovie(id: Int): Flow<Resource<Movie>> {
        return flow {

            emit(Resource.Loading(true))

            val movieEntity = movieDataBase.movieDao.getMovieById(id)

            if (movieEntity != null) {
                emit(Resource.Success(data = movieEntity.toMovie(movieEntity.category)))
                emit(Resource.Loading(false))
                return@flow
            }
            emit(Resource.Error(message = "An error occurred"))
            emit(Resource.Loading(false))
        }
    }
}
