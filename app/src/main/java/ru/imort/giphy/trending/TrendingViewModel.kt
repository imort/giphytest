package ru.imort.giphy.trending

import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import ru.imort.giphy.api.GiphyApi
import timber.log.Timber

class TrendingViewModel(
    private val api: GiphyApi
) : ViewModel() {

    private val disposables = CompositeDisposable()

    init {
        fetch()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    private fun fetch() {
        disposables += api.trending()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d(it.toString())
            }, {
                Timber.e(it)
            })
    }
}
