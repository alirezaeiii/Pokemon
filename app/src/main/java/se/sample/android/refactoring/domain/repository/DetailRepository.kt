package se.sample.android.refactoring.domain.repository

import io.reactivex.Single
import se.sample.android.refactoring.domain.DetailWrapper

interface DetailRepository {

    fun getDetails(pokemonId: Int): Single<DetailWrapper>
}