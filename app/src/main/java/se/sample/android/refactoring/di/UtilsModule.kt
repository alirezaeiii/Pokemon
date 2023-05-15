package se.sample.android.refactoring.di

import dagger.Binds
import dagger.Module
import se.sample.android.refactoring.util.schedulars.BaseSchedulerProvider
import se.sample.android.refactoring.util.schedulars.SchedulerProvider

@Module
abstract class UtilsModule {

    @Binds
    internal abstract fun bindSchedulerProvider(schedulerProvider: SchedulerProvider): BaseSchedulerProvider
}