package se.appshack.android.refactoring.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import org.apache.commons.lang3.StringUtils
import se.appshack.android.refactoring.domain.Pokemon
import se.appshack.android.refactoring.network.GenusResponseModel
import se.appshack.android.refactoring.network.PokemonDetailsResponse
import se.appshack.android.refactoring.network.PokemonService
import se.appshack.android.refactoring.util.schedulars.BaseSchedulerProvider
import javax.inject.Inject
import se.appshack.android.refactoring.viewmodels.DetailViewModel.DetailWrapper

/**
 * DetailViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 */
class DetailViewModel(
    api: PokemonService,
    schedulerProvider: BaseSchedulerProvider,
    pokemonId: Int
) : BaseViewModel<DetailWrapper, DetailWrapper>(schedulerProvider) {

    override val requestObservable: Observable<DetailWrapper> =
        Observable.zip(api.getPokemonDetails(pokemonId),
            api.getPokemonSpecies(pokemonId).map { it.genera },
            BiFunction<PokemonDetailsResponse, List<GenusResponseModel>, DetailWrapper> {
                    pokemonDetail, genusModels -> DetailWrapper(pokemonDetail,
                    pokemonDetail.types.joinToString { StringUtils.capitalize(it.type.name) },
                    genusModels.find { genusModel -> genusModel.language.name == "en" }?.genus
                )
            })

    override fun getSuccessResult(it: DetailWrapper): DetailWrapper = it

    init {
        sendRequest()
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