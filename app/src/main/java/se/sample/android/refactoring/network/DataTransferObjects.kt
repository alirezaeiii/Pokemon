package se.sample.android.refactoring.network

import com.squareup.moshi.Json
import org.apache.commons.lang3.StringUtils
import se.sample.android.refactoring.domain.Pokemon
import se.sample.android.refactoring.repository.MainRepositoryImpl.Companion.LIMIT
import se.sample.android.refactoring.util.nextInt
import java.util.*
import kotlin.collections.ArrayList

class NamedResponseModel(
        val name: String,
        val url: String
)

class PokemonListResponse(
        val count: Int,
        val next: String,
        val previous: String?,
        val results: List<NamedResponseModel>
)

class GenusResponseModel(
        val genus: String,
        val language: NamedResponseModel
)

class PokemonSpeciesResponse(
        val id: Int,
        val name: String,
        val genera: List<GenusResponseModel>
)

class PokemonTypeModel(
        val slot: Int,
        val type: NamedResponseModel
)

class PokemonSpritesModel(
        @Json(name = "front_default")
        val urlFront: String,
        @Json(name = "back_default")
        val urlBack: String
)

class PokemonDetailsResponse(
        val id: Int,
        val name: String,
        val height: Int,
        val weight: Int,
        val species: NamedResponseModel,
        val types: List<PokemonTypeModel>,
        val sprites: PokemonSpritesModel
)

fun List<NamedResponseModel>.asDomainModel(): List<Pokemon> {
    val ids = ArrayList<Int>()
    var i = 0
    val pairLimit : Pair<Int, Int> = if (size == LIMIT) {
        Pair(20, size)
    } else {
        Pair(1, 1)
    }
    while (i < pairLimit.first) {
        val id = Random().nextInt(1..pairLimit.second)
        if (!ids.contains(id)) {
            ids.add(id)
            i++
        }
    }
    ids.sortWith(compareBy { it })

    val pokemonList = ArrayList<Pokemon>()
    for (index in ids) {
        val namedResponseModel = this[index - 1]
        val id = namedResponseModel.url.substring(
                namedResponseModel.url.indexOf("pokemon/") + 8,
                namedResponseModel.url.length - 1
        ).toInt()
        pokemonList.add(
                Pokemon(
                        id = id,
                        name = StringUtils.capitalize(namedResponseModel.name)
                )
        )
    }
    return pokemonList
}