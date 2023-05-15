package se.sample.android.refactoring.repository

import io.reactivex.Single
import io.reactivex.functions.BiFunction
import org.apache.commons.lang3.StringUtils
import se.sample.android.refactoring.domain.repository.DetailRepository
import se.sample.android.refactoring.domain.DetailWrapper
import se.sample.android.refactoring.network.GenusResponseModel
import se.sample.android.refactoring.network.PokemonDetailsResponse
import se.sample.android.refactoring.network.PokemonService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailRepositoryImpl @Inject constructor(private val api: PokemonService) : DetailRepository {

    override fun getDetails(pokemonId: Int): Single<DetailWrapper> =
        Single.zip(api.getPokemonDetails(pokemonId),
            api.getPokemonSpecies(pokemonId).map { it.genera },
            BiFunction<PokemonDetailsResponse, List<GenusResponseModel>, DetailWrapper> { pokemonDetail, genusModels ->
                DetailWrapper(
                    pokemonDetail,
                    pokemonDetail.types.joinToString { StringUtils.capitalize(it.type.name) },
                    genusModels.find { genusModel -> genusModel.language.name == "en" }?.genus
                )
            })
}