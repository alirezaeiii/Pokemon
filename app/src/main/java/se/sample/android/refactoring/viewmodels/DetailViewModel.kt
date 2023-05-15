package se.sample.android.refactoring.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import se.sample.android.refactoring.domain.repository.DetailRepository
import se.sample.android.refactoring.domain.DetailWrapper
import se.sample.android.refactoring.domain.Pokemon
import se.sample.android.refactoring.util.schedulars.BaseSchedulerProvider
import javax.inject.Inject

/**
 * DetailViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 */
class DetailViewModel(
    repository: DetailRepository,
    schedulerProvider: BaseSchedulerProvider,
    pokemonId: Int
) : BaseViewModel<DetailWrapper>(schedulerProvider, repository.getDetails(pokemonId)) {

    /**
     * Factory for constructing DetailViewModel with parameter
     */
    class Factory @Inject constructor(
        private val repository: DetailRepository,
        private val schedulerProvider: BaseSchedulerProvider,
        private val pokemon: Pokemon
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(repository, schedulerProvider, pokemon.id) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}