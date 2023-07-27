package com.mutkuensert.movee.domain.account

import com.github.michaelbull.result.Result
import com.mutkuensert.movee.data.account.dto.FavoriteMoviesDto
import com.mutkuensert.movee.data.authentication.dto.AccountDetailsDto
import com.mutkuensert.movee.domain.Failure

interface AccountRepository {
    suspend fun getAccountDetails(): Result<AccountDetailsDto, Failure>
    suspend fun getFavoriteMovies(): Result<FavoriteMoviesDto, Failure>
}