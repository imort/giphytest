package ru.imort.giphy.trending

import ru.imort.giphy.api.GiphyObject
import ru.imort.giphy.api.GiphyPagination

/**
 * Created on 2019-09-29.
 *
 * @author yaroslav.nastyuk
 */
data class TrendingViewState(
    val data: List<GiphyObject>,        // The items displayed in the recyclerView
    val firstPageLoading: Boolean,      // Show the loading indicator instead of recyclerView
    val firstPageError: Throwable?,     // Show an error view if != null
    val refreshLoading: Boolean,        // Shows the pull-to-refresh indicator

    val pagination: GiphyPagination?,   // Used for next page loading
    val nextPageLoading: Boolean,       // Shows the loading indicator for pagination
    val nextPageError: Throwable?       // if != null, shows error toast that pagination failed
) {
    override fun toString(): String {
        return "TrendingViewState(data.size=${data.size}," +
                "\n firstPageLoading=$firstPageLoading," +
                "\n firstPageError=$firstPageError," +
                "\n pullToRefreshLoading=$refreshLoading," +
                "\n pagination=$pagination," +
                "\n nextPageLoading=$nextPageLoading," +
                "\n nextPageError=$nextPageError"
    }

    companion object {
        val initialState = TrendingViewState(
            listOf(), false, null,
            false, null, false,
            null
        )
    }
}