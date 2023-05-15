package se.sample.android.refactoring.domain

import se.sample.android.refactoring.network.PokemonDetailsResponse

class DetailWrapper(
    val pokemonDetail: PokemonDetailsResponse,
    val type: String,
    val genus: String?
)