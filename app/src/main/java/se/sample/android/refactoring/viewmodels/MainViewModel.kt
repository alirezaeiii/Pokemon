package se.sample.android.refactoring.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import se.sample.android.refactoring.domain.repository.MainRepository
import se.sample.android.refactoring.domain.Pokemon
import se.sample.android.refactoring.util.schedulars.BaseSchedulerProvider
import javax.inject.Inject

/**
 * MainViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 */
class MainViewModel(
    repository: MainRepository,
    schedulerProvider: BaseSchedulerProvider
) : BaseViewModel<List<Pokemon>>(schedulerProvider,
    repository.getPokemonList()) {

    /**
     * Factory for constructing MainViewModel with parameter
     */
    class Factory @Inject constructor(
        private val repository: MainRepository,
        private val schedulerProvider: BaseSchedulerProvider
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(repository, schedulerProvider) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}