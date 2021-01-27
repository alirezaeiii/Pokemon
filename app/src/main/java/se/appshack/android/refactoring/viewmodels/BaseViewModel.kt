package se.appshack.android.refactoring.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import se.appshack.android.refactoring.util.EspressoIdlingResource
import se.appshack.android.refactoring.util.Resource
import se.appshack.android.refactoring.util.schedulars.BaseSchedulerProvider
import timber.log.Timber

abstract class BaseViewModel<T, R>(private val schedulerProvider: BaseSchedulerProvider) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _liveData = MutableLiveData<Resource<T>>()
    val liveData: LiveData<Resource<T>>
        get() = _liveData

    protected abstract val requestObservable: Observable<R>

    protected abstract fun getSuccessResult(it: R): T

    fun sendRequest() {
        _liveData.value = Resource.Loading()
        composeObservable { requestObservable }
            .subscribe({
                _liveData.postValue(Resource.Success(getSuccessResult(it)))
            }) {
                _liveData.postValue(Resource.Failure(it.localizedMessage))
                Timber.e(it)
            }.also { compositeDisposable.add(it) }
    }

    private fun <T> composeObservable(task: () -> Observable<T>): Observable<T> = task()
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