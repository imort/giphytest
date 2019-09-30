package ru.imort.giphy.detail

import ru.imort.giphy.api.GiphyObject

/**
 * Created on 2019-09-29.
 *
 * @author yaroslav.nastyuk
 */
data class DetailViewState(
    val loading: Boolean,
    val data: GiphyObject?,
    val error: Throwable?
)