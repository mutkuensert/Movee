package movee.presentation.movie.navigation

import movee.presentation.navigation.FeatureNavigationBuilder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import movee.presentation.navigation.navigator.MovieNavigator

@Module
@InstallIn(SingletonComponent::class)
interface MovieModule {

    @Binds
    @IntoSet
    fun bindMovieNavigationBuilder(builder: MovieNavigationBuilder): FeatureNavigationBuilder

    @Binds
    fun bindMovieNavigator(navigator: MovieNavigatorImpl): MovieNavigator
}