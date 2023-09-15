package movee.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import movee.data.library.session.SessionManagerImpl
import movee.data.library.user.UserManagerImpl
import movee.domain.library.SessionManager
import movee.domain.library.UserManager

@Module
@InstallIn(SingletonComponent::class)
interface LibraryModule {

    @Binds
    @Singleton
    fun provideSessionManager(sessionManagerImpl: SessionManagerImpl): SessionManager

    @Binds
    @Singleton
    fun provideUserManager(userManagerImpl: UserManagerImpl): UserManager
}