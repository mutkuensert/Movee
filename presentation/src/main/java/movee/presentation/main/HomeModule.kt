package movee.presentation.main

import movee.presentation.navigation.navigator.MainNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface HomeModule {

    @Binds
    fun bindMainNavigatorImpl(navigator: HomeNavigatorImpl): MainNavigator
}