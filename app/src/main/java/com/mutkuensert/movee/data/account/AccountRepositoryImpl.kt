package com.mutkuensert.movee.data.account

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Result
import com.mutkuensert.movee.data.account.dto.FavoriteMoviesDto
import com.mutkuensert.movee.data.authentication.dto.AccountDetailsDto
import com.mutkuensert.movee.domain.Failure
import com.mutkuensert.movee.domain.account.AccountRepository
import com.mutkuensert.movee.library.session.SessionManager
import com.mutkuensert.movee.network.toResult
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountApi: AccountApi,
    private val sessionManager: SessionManager,
) : AccountRepository {
    override suspend fun getAccountDetails(): Result<AccountDetailsDto, Failure> {
        val sessionId = sessionManager.getSessionId()
            ?: return Err(Failure(statusMessage = "${SessionManager::getSessionId.name} returned null."))

        return accountApi.getAccountDetails(sessionId).toResult()
    }

    override suspend fun getFavoriteMovies(): Result<FavoriteMoviesDto, Failure> {
        return accountApi.getFavoriteMovies().toResult()
    }
}