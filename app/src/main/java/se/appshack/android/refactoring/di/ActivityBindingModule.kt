package se.appshack.android.refactoring.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import se.appshack.android.refactoring.ui.MainActivity

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector(
        modules = [MainModule::class,
            DetailModule::class]
    )
    internal abstract fun mainActivity(): MainActivity
}