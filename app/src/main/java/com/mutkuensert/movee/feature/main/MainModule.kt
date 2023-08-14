package com.mutkuensert.movee.feature.main

import com.mutkuensert.movee.navigation.navigator.MainNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface MainModule {

    @Binds
    fun bindMainNavigatorImpl(navigator: MainNavigatorImpl): MainNavigator
}