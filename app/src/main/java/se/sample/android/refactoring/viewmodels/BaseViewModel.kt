package se.sample.android.refactoring.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import se.sample.android.refactoring.util.EspressoIdlingResource
import se.sample.android.refactoring.util.Resource
import se.sample.android.refactoring.util.schedulars.BaseSchedulerProvider
import timber.log.Timber

abstract class BaseViewModel<T>(
    private val schedulerProvider: BaseSchedulerProvider,
    private val requestSingle: Single<T>
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _liveData = MutableLiveData<Resource<T>>()
    val liveData: LiveData<Resource<T>>
        get() = _liveData

    init {
        sendRequest()
    }

    fun sendRequest() {
        _liveData.value = Resource.Loading()
        composeSingle { requestSingle }
            .subscribe({
                _liveData.postValue(Resource.Success(it))
            }) {
                _liveData.postValue(Resource.Failure(it.localizedMessage))
                Timber.e(it)
            }.also { compositeDisposable.add(it) }
    }

    private inline fun <T> composeSingle(task: () -> Single<T>): Single<T> = task()
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