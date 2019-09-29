package ru.imort.giphy.trending

import ru.imort.giphy.api.TrendingResponse
import timber.log.Timber

/**
 * Created on 2019-09-29.
 *
 * @author yaroslav.nastyuk
 */
sealed class TrendingPartialState {
    abstract fun reduce(prev: TrendingViewState): TrendingViewState

    object FirstPageLoading : TrendingPartialState() {
        override fun reduce(prev: TrendingViewState) = prev.copy(
            firstPageLoading = true,
            firstPageError = null,
            refreshLoading = false,
            pagination = null
        )
    }

    data class FirstPageError(val throwable: Throwable) : TrendingPartialState() {
        override fun reduce(prev: TrendingViewState) = prev.copy(
            firstPageLoading = false,
            firstPageError = throwable,
            refreshLoading = false
        )
    }

    data class FirstPageData(val response: TrendingResponse) : TrendingPartialState() {
        override fun reduce(prev: TrendingViewState) = prev.copy(
            firstPageLoading = false,
            firstPageError = null,
            refreshLoading = false,
            data = response.data,
            pagination = response.pagination
        )
    }

    object NextPageLoading : TrendingPartialState() {
        override fun reduce(prev: TrendingViewState) =
            prev.copy(nextPageLoading = true, nextPageError = null)
    }

    data class NextPageError(val throwable: Throwable) : TrendingPartialState() {
        override fun reduce(prev: TrendingViewState) = prev.copy(
            nextPageLoading = false,
            nextPageError = throwable
        )
    }

    data class NextPageData(val response: TrendingResponse) : TrendingPartialState() {
        override fun reduce(prev: TrendingViewState): TrendingViewState =
            if (prev.pagination?.next() == response.pagination.offset) {
                prev.copy(
                    nextPageLoading = false,
                    nextPageError = null,
                    data = prev.data + response.data,
                    pagination = response.pagination
                )
            } else {
                Timber.e("Dropping wrong next data")
                prev.copy(
                    nextPageLoading = false
                )
            }
    }

    object RefreshLoading : TrendingPartialState() {
        override fun reduce(prev: TrendingViewState) = prev.copy(
            refreshLoading = true
        )
    }

    data class RefreshError(val throwable: Throwable) : TrendingPartialState() {
        override fun reduce(prev: TrendingViewState) = prev.copy(
            refreshLoading = false,
            firstPageError = throwable
        )
    }

    data class RefreshData(val response: TrendingResponse) : TrendingPartialState() {
        override fun reduce(prev: TrendingViewState) = prev.copy(
            refreshLoading = false,
            data = response.data,
            pagination = response.pagination
        )
    }
}