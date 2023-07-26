package com.mutkuensert.movee.data.authentication

import com.mutkuensert.movee.domain.login.AuthenticationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AuthenticationModule {

    @Singleton
    @Binds
    fun bindLoginRepository(loginRepositoryImpl: AuthenticationRepositoryImpl): AuthenticationRepository
}