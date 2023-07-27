package com.mutkuensert.movee.data.account

import com.mutkuensert.movee.domain.account.AccountRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AccountModule {

    @Singleton
    @Binds
    fun provideAccountRepository(accountRepositoryImpl: AccountRepositoryImpl): AccountRepository
}