package com.mutkuensert.movee.feature.tvshow.navigation

import com.mutkuensert.movee.navigation.FeatureNavigationBuilder
import com.mutkuensert.movee.navigation.navigator.TvShowNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface TvShowModule {

    @Binds
    @IntoSet
    fun bindTvShowNavigationBuilder(builder: TvShowNavigationBuilder): FeatureNavigationBuilder

    @Binds
    fun bindTvShowNavigator(navigator: TvShowNavigatorImpl): TvShowNavigator
}