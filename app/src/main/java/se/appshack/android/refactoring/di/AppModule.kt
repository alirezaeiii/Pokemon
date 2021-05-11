package se.appshack.android.refactoring.di

import dagger.Binds
import dagger.Module
import se.appshack.android.refactoring.util.schedulars.BaseSchedulerProvider
import se.appshack.android.refactoring.util.schedulars.SchedulerProvider

@Module
abstract class AppModule {

    @Binds
    internal abstract fun bindSchedulerProvider(schedulerProvider: SchedulerProvider): BaseSchedulerProvider
}