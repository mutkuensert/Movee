package movee.presentation.main

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import movee.presentation.navigation.navigator.MainNavigator

@Module
@InstallIn(SingletonComponent::class)
interface HomeModule {

    @Binds
    fun bindMainNavigatorImpl(navigator: HomeNavigatorImpl): MainNavigator
}