package com.mutkuensert.movee.feature.multisearch.navigation

import com.mutkuensert.movee.navigation.FeatureNavigationBuilder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface MultiSearchModule {

    @Binds
    @IntoSet
    fun bindMultiSearchNavigationBuilder(builder: MultiSearchNavigationBuilder): FeatureNavigationBuilder
}