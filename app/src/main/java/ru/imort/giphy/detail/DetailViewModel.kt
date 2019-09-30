package ru.imort.giphy.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ru.imort.giphy.api.GiphyApi

class DetailViewModel(
    private val api: GiphyApi
) : ViewModel() {

    private val _viewState = MutableLiveData<DetailViewState>()
    val viewState: LiveData<DetailViewState> = _viewState

    private val actionSubject = PublishSubject.create<DetailViewAction>().toSerialized()
    private val disposables = CompositeDisposable()

    init {
        val initialState = DetailViewState(false, null, null)
        disposables += actionSubject
            .switchMap {
                when (it) {
                    is DetailViewAction.Load -> load(it)
                }
            }
            .scan(initialState) { state, partial ->
                partial.reduce(state)
            }
            .subscribe({
                _viewState.postValue(it)
            }, {
                throw IllegalStateException("Will never happen", it)
            })
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun dispatch(action: DetailViewAction) {
        actionSubject.onNext(action)
    }

    private fun load(action: DetailViewAction.Load): Observable<DetailPartialState> =
        if (action.giphy == null) {
            api.getById(action.id)
                .map<DetailPartialState> {
                    DetailPartialState.Data(it.data)
                }
                .toObservable()
                .startWith(DetailPartialState.Loading)
                .onErrorReturn { DetailPartialState.Error(it) }
                .subscribeOn(Schedulers.io())
        } else {
            Observable.just(DetailPartialState.Data(action.giphy))
        }
}
