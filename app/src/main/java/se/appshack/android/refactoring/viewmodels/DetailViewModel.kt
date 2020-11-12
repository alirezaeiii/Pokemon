package se.appshack.android.refactoring.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.apache.commons.lang3.StringUtils
import se.appshack.android.refactoring.domain.Pokemon
import se.appshack.android.refactoring.network.PokemonService
import se.appshack.android.refactoring.network.PokemonDetailsResponse
import se.appshack.android.refactoring.util.Resource
import se.appshack.android.refactoring.util.schedulars.BaseSchedulerProvider
import timber.log.Timber
import javax.inject.Inject

/**
 * DetailViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 */
class DetailViewModel(
    private val api: PokemonService,
    schedulerProvider: BaseSchedulerProvider,
    private val pokemonId: Int
) : BaseViewModel(schedulerProvider) {

    private val _liveData = MutableLiveData<Resource<PokemonDetailsResponse>>()
    val liveData: LiveData<Resource<PokemonDetailsResponse>>
        get() = _liveData

    private val _types = MutableLiveData<String>()
    val types: LiveData<String>
        get() = _types

    private val _genus = MutableLiveData<Resource<String>>()
    val genus: LiveData<Resource<String>>
        get() = _genus

    init {
        showPokemonDetails()
    }

    fun showPokemonDetails() {
        arrayOf(composeObservable { api.getPokemonDetails(pokemonId) }
            .doOnSubscribe { _liveData.value = Resource.Loading() }
            .subscribe({ details ->
                _liveData.postValue(Resource.Success(details))
                details.types.toMutableList().sortWith(compareBy { it.slot })
                _types.postValue(details.types.joinToString { StringUtils.capitalize(it.type.name) })
            }) {
                _liveData.postValue(Resource.Failure(it.localizedMessage))
                Timber.e(it)
            }
            , composeObservable { api.getPokemonSpecies(pokemonId) }.map { it.genera }
                .doOnSubscribe { _genus.value = Resource.Loading() }
                .subscribe({
                    _genus.postValue(Resource.Success(it.find { genusModel -> genusModel.language.name == "en" }?.genus))
                }) {
                    _genus.postValue(Resource.Failure(it.localizedMessage))
                    Timber.e(it)
                }).also { compositeDisposable.addAll(*it) }
    }

    /**
     * Factory for constructing DetailViewModel with parameter
     */
    class Factory @Inject constructor(
        private val api: PokemonService,
        private val schedulerProvider: BaseSchedulerProvider,
        val pokemon: Pokemon
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(api, schedulerProvider, pokemon.id) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}