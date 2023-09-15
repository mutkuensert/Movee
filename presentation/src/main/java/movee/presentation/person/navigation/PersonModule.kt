package movee.presentation.person.navigation

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import movee.presentation.navigation.FeatureNavigationBuilder
import movee.presentation.navigation.navigator.PersonNavigator

@Module
@InstallIn(SingletonComponent::class)
interface PersonModule {

    @Binds
    @IntoSet
    fun bindPersonNavigationBuilder(builder: PersonNavigationBuilder): FeatureNavigationBuilder

    @Binds
    fun bindPersonNavigator(navigator: PersonNavigatorImpl): PersonNavigator
}