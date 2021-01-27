package se.appshack.android.refactoring.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.Observable
import se.appshack.android.refactoring.domain.Pokemon
import se.appshack.android.refactoring.network.NamedResponseModel
import se.appshack.android.refactoring.network.PokemonService
import se.appshack.android.refactoring.network.asDomainModel
import se.appshack.android.refactoring.util.schedulars.BaseSchedulerProvider
import javax.inject.Inject

/**
 * MainViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 */
class MainViewModel(
    api: PokemonService,
    schedulerProvider: BaseSchedulerProvider
) : BaseViewModel<List<Pokemon>, List<NamedResponseModel>>(schedulerProvider) {

    override val requestObservable: Observable<List<NamedResponseModel>> =
        api.getPokemonList(LIMIT).map { it.results }

    override fun getSuccessResult(it: List<NamedResponseModel>): List<Pokemon> = it.asDomainModel()

    init {
        sendRequest()
    }

    /**
     * Factory for constructing MainViewModel with parameter
     */
    class Factory @Inject constructor(
        private val api: PokemonService,
        private val schedulerProvider: BaseSchedulerProvider
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(api, schedulerProvider) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }

    companion object {
        private const val LIMIT = 151
    }
}