package se.appshack.android.refactoring.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import org.apache.commons.lang3.StringUtils
import se.appshack.android.refactoring.domain.Pokemon
import se.appshack.android.refactoring.network.GenusResponseModel
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

    private val _liveData = MutableLiveData<Resource<DetailWrapper>>()
    val liveData: LiveData<Resource<DetailWrapper>>
        get() = _liveData

    init {
        showPokemonDetails()
    }

    fun showPokemonDetails() {
        _liveData.value = Resource.Loading()
        val source1 = api.getPokemonDetails(pokemonId)
        val source2 = api.getPokemonSpecies(pokemonId).map { it.genera }

        composeObservable { Observable.zip(source1, source2,
            BiFunction<PokemonDetailsResponse, List<GenusResponseModel>, DetailWrapper> {
                    pokemonDetail, genusModels -> DetailWrapper(pokemonDetail,
                    pokemonDetail.types.joinToString { StringUtils.capitalize(it.type.name) },
                    genusModels.find { genusModel -> genusModel.language.name == "en" }?.genus) })
        }.subscribe({
            _liveData.postValue(Resource.Success(it))
        }) {
            _liveData.postValue(Resource.Failure(it.localizedMessage))
            Timber.e(it)
        }.also { compositeDisposable.add(it) }
    }

    class DetailWrapper(
        val pokemonDetail: PokemonDetailsResponse,
        val type: String,
        val genus: String?
    )

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