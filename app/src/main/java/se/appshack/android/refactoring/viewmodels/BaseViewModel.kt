package se.appshack.android.refactoring.viewmodels

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import se.appshack.android.refactoring.util.EspressoIdlingResource
import se.appshack.android.refactoring.util.schedulars.BaseSchedulerProvider

open class BaseViewModel(private val schedulerProvider: BaseSchedulerProvider) : ViewModel() {

    protected val compositeDisposable = CompositeDisposable()

    protected fun <T> composeObservable(task: () -> Observable<T>): Observable<T> = task()
        .doOnSubscribe { EspressoIdlingResource.increment() } // App is busy until further notice
        .subscribeOn(schedulerProvider.io())
        .observeOn(schedulerProvider.ui())
        .doFinally {
            if (!EspressoIdlingResource.countingIdlingResource.isIdleNow) {
                EspressoIdlingResource.decrement() // Set app as idle.
            }
        }

    /**
     * Called when the ViewModel is dismantled.
     * At this point, we want to cancel all disposables;
     * otherwise we end up with processes that have nowhere to return to
     * using memory and resources.
     */
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}