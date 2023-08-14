package com.mutkuensert.movee.feature.person.navigation

import com.mutkuensert.movee.navigation.FeatureNavigationBuilder
import com.mutkuensert.movee.navigation.navigator.PersonNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface PersonModule {

    @Binds
    @IntoSet
    fun bindPersonNavigationBuilder(builder: PersonNavigationBuilder): FeatureNavigationBuilder

    @Binds
    fun bindPersonNavigator(navigator: PersonNavigatorImpl): PersonNavigator
}