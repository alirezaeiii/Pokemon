package se.appshack.android.refactoring.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import se.appshack.android.refactoring.domain.Pokemon
import se.appshack.android.refactoring.network.PokemonService
import se.appshack.android.refactoring.network.asDomainModel
import se.appshack.android.refactoring.util.Resource
import se.appshack.android.refactoring.util.schedulars.BaseSchedulerProvider
import timber.log.Timber
import javax.inject.Inject

/**
 * MainViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 */
class MainViewModel(
    private val api: PokemonService,
    schedulerProvider: BaseSchedulerProvider
) : BaseViewModel(schedulerProvider) {

    private val _liveData = MutableLiveData<Resource<List<Pokemon>>>()
    val liveData: LiveData<Resource<List<Pokemon>>>
        get() = _liveData

    init {
        showPokemonList()
    }

    fun showPokemonList() {
        _liveData.value = Resource.Loading()
        composeObservable { api.getPokemonList(LIMIT) }.map { it.results }
            .subscribe({
                _liveData.postValue(Resource.Success(it.asDomainModel()))
            }) {
                _liveData.postValue(Resource.Failure(it.localizedMessage))
                Timber.e(it)
            }.also { compositeDisposable.add(it) }
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