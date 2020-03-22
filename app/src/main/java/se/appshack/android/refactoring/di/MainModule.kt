package se.appshack.android.refactoring.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import se.appshack.android.refactoring.ui.MainFragment
import se.appshack.android.refactoring.viewmodels.MainViewModel

@Module
abstract class MainModule {

    @ContributesAndroidInjector
    internal abstract fun mainFragment(): MainFragment

    @Binds
    internal abstract fun bindViewModelFactory(factory: MainViewModel.Factory): ViewModelProvider.Factory
}