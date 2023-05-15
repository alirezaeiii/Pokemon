package se.sample.android.refactoring.domain.repository

import io.reactivex.Single
import se.sample.android.refactoring.domain.Pokemon

interface MainRepository {

    fun getPokemonList(): Single<List<Pokemon>>
}