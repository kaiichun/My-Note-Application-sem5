package alvin.note.core.di

import alvin.note.core.service.AuthService
import alvin.note.core.service.StorageService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Provides
    @Singleton
    fun provideAuthService(): AuthService {
        return AuthService()
    }

    @Provides
    @Singleton
    fun provideStorageService(authService: AuthService): StorageService {
        return StorageService(authService)
    }
}