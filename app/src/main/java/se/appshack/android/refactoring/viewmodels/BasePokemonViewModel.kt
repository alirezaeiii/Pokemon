package se.appshack.android.refactoring.viewmodels

import io.reactivex.Single
import se.appshack.android.refactoring.util.schedulars.BaseSchedulerProvider

open class BasePokemonViewModel<T, R : T>(
        schedulerProvider: BaseSchedulerProvider,
        requestSingle: Single<R>
) : BaseViewModel<T, R>(schedulerProvider, requestSingle) {

    override fun getSuccessResult(it: R): T = it
}