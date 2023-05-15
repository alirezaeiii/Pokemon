package se.sample.android.refactoring.repository

import io.reactivex.Single
import se.sample.android.refactoring.domain.repository.MainRepository
import se.sample.android.refactoring.domain.Pokemon
import se.sample.android.refactoring.network.PokemonService
import se.sample.android.refactoring.network.asDomainModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepositoryImpl @Inject constructor(private val api: PokemonService) : MainRepository {

    override fun getPokemonList(): Single<List<Pokemon>> = api.getPokemonList(LIMIT)
        .map { it.results }.map { it.asDomainModel() }

    companion object {
        const val LIMIT = 151
    }
}