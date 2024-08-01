package alvin.note.core.di

import alvin.note.core.service.AuthService
import alvin.note.data.repository.NoteRepository
import alvin.note.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule{
    @Provides
    @Singleton
    fun provideTodoRepository(authService: AuthService): NoteRepository {
        return NoteRepository(authService)
    }
    @Provides
    @Singleton
    fun provideUserRepository(authService: AuthService): UserRepository {
        return UserRepository(authService)
    }
}