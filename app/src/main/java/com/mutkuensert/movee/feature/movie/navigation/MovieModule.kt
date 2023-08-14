package com.mutkuensert.movee.feature.movie.navigation

import com.mutkuensert.movee.navigation.FeatureNavigationBuilder
import com.mutkuensert.movee.navigation.navigator.MovieNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface MovieModule {

    @Binds
    @IntoSet
    fun bindMovieNavigationBuilder(builder: MovieNavigationBuilder): FeatureNavigationBuilder

    @Binds
    fun bindMovieNavigator(navigator: MovieNavigatorImpl): MovieNavigator
}