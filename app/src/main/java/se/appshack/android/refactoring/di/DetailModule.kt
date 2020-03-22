package se.appshack.android.refactoring.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import se.appshack.android.refactoring.R
import se.appshack.android.refactoring.domain.Pokemon
import se.appshack.android.refactoring.ui.DetailFragment
import se.appshack.android.refactoring.ui.DetailFragmentArgs
import se.appshack.android.refactoring.ui.MainActivity
import se.appshack.android.refactoring.viewmodels.DetailViewModel

@Module
abstract class DetailModule {

    @ContributesAndroidInjector
    internal abstract fun detailFragment(): DetailFragment

    @Binds
    internal abstract fun bindViewModelFactory(factory: DetailViewModel.Factory): ViewModelProvider.Factory

    @Module
    companion object {

        @Provides
        @JvmStatic
        internal fun providePokemon(activity: MainActivity): Pokemon {
            val navHostFragment = activity.supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            val fragment = navHostFragment?.childFragmentManager?.fragments?.get(0)
            return DetailFragmentArgs.fromBundle(fragment?.arguments!!).pokemon
        }
    }
}