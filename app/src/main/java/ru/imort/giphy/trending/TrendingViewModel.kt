package ru.imort.giphy.trending

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import ru.imort.giphy.api.GiphyApi

class TrendingViewModel(
    private val api: GiphyApi
) : ViewModel() {

    private val _viewState = MutableLiveData<TrendingViewState>()
    val viewState: LiveData<TrendingViewState> = _viewState

    private val actionSubject = PublishSubject.create<TrendingViewAction>().toSerialized()
    private var actionDisposable: Disposable? = null
    private val disposables = CompositeDisposable()

    init {
        disposables += actionSubject
            .flatMap {
                when (it) {
                    is TrendingViewAction.Load -> load()
                    is TrendingViewAction.LoadForward -> next(it.offset)
                    is TrendingViewAction.Refresh -> refresh()
                }
            }
            .scan(TrendingViewState.initialState) { state, partial ->
                partial.reduce(state)
            }
            .subscribe({
                _viewState.postValue(it)
            }, {
                throw IllegalStateException("Will never happen", it)
            })
        actionSubject.onNext(TrendingViewAction.Load) // initial load
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun bindActions(actions: Observable<TrendingViewAction>) {
        actionDisposable = actions.subscribe(actionSubject::onNext)
    }

    fun unbindActions() {
        if (actionDisposable?.isDisposed == false) {
            actionDisposable?.dispose()
        }
    }

    private fun load(): Observable<TrendingPartialState> =
        api.trending()
            .map<TrendingPartialState> {
                TrendingPartialState.FirstPageData(it)
            }
            .toObservable()
            .startWith(TrendingPartialState.FirstPageLoading)
            .onErrorReturn { TrendingPartialState.FirstPageError(it) }
            .subscribeOn(Schedulers.io())

    private fun next(offset: Int): Observable<TrendingPartialState> =
        api.trending(offset = offset)
            .map<TrendingPartialState> {
                TrendingPartialState.NextPageData(it)
            }
            .toObservable()
            .startWith(TrendingPartialState.NextPageLoading)
            .onErrorReturn { TrendingPartialState.NextPageError(it) }
            .subscribeOn(Schedulers.io())

    private fun refresh(): Observable<TrendingPartialState> =
        api.trending()
            .map<TrendingPartialState> {
                TrendingPartialState.RefreshData(it)
            }
            .toObservable()
            .startWith(TrendingPartialState.RefreshLoading)
            .onErrorReturn { TrendingPartialState.RefreshError(it) }
            .subscribeOn(Schedulers.io())
}
