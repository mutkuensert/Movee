package com.mutkuensert.movee.domain.account

import com.github.michaelbull.result.Result
import com.mutkuensert.movee.domain.Failure
import com.mutkuensert.movee.library.user.UserDetails

interface AccountRepository {
    suspend fun getUserDetails(): Result<UserDetails, Failure>
    suspend fun fetchFavoriteMoviesAndTvShows()
    suspend fun addMovieToFavorites(isFavorite: Boolean, movieId: Int)
    suspend fun addTvShowToFavorites(isFavorite: Boolean, movieId: Int)
}