package se.appshack.android.refactoring.viewmodels

import io.reactivex.Single
import se.appshack.android.refactoring.util.schedulars.BaseSchedulerProvider

open class BaseDetailViewModel<T>(
        schedulerProvider: BaseSchedulerProvider,
        requestSingle: Single<T>
) : BaseViewModel<T, T>(schedulerProvider, requestSingle) {

    override fun getSuccessResult(it: T): T = it
}