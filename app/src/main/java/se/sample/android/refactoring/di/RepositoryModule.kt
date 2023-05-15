package se.sample.android.refactoring.di

import dagger.Binds
import dagger.Module
import se.sample.android.refactoring.domain.repository.DetailRepository
import se.sample.android.refactoring.domain.repository.MainRepository
import se.sample.android.refactoring.repository.DetailRepositoryImpl
import se.sample.android.refactoring.repository.MainRepositoryImpl
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    internal abstract fun bindMainRepository(repository: MainRepositoryImpl): MainRepository

    @Singleton
    @Binds
    internal abstract fun bindDetailRepository(repository: DetailRepositoryImpl): DetailRepository
}