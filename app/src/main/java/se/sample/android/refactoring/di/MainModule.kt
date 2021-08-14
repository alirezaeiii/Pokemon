package se.sample.android.refactoring.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import se.sample.android.refactoring.ui.MainFragment
import se.sample.android.refactoring.viewmodels.MainViewModel

@Module
abstract class MainModule {

    @ContributesAndroidInjector
    internal abstract fun mainFragment(): MainFragment

    @Binds
    internal abstract fun bindViewModelFactory(factory: MainViewModel.Factory): ViewModelProvider.Factory
}