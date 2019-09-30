package ru.imort.giphy.detail

import ru.imort.giphy.api.GiphyObject

/**
 * Created for extensibility, actually basic LCE w/o partial states is enough
 * Created on 2019-09-29.
 *
 * @author yaroslav.nastyuk
 */
sealed class DetailPartialState {
    abstract fun reduce(prev: DetailViewState): DetailViewState

    object Loading : DetailPartialState() {
        override fun reduce(prev: DetailViewState) = DetailViewState(
            loading = true,
            data = null,
            error = null
        )
    }

    data class Error(val throwable: Throwable) : DetailPartialState() {
        override fun reduce(prev: DetailViewState) = DetailViewState(
            loading = false,
            data = null,
            error = throwable
        )
    }

    data class Data(val giphy: GiphyObject) : DetailPartialState() {
        override fun reduce(prev: DetailViewState) = DetailViewState(
            loading = false,
            data = giphy,
            error = null
        )
    }
}