package movee.presentation.login.navigation

import movee.presentation.navigation.FeatureNavigationBuilder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface LoginModule {

    @Binds
    @IntoSet
    fun bindLoginNavigationBuilder(builder: LoginNavigationBuilder): FeatureNavigationBuilder
}