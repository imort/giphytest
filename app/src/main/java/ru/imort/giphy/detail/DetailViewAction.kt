package ru.imort.giphy.detail

import ru.imort.giphy.api.GiphyObject

/**
 * Represent Intent in the MVI pattern. Named as such to avoid conflict with android Intent
 * Created on 2019-09-29.
 *
 * @author yaroslav.nastyuk
 */
sealed class DetailViewAction {
    data class Load(val id: String, val giphy: GiphyObject?) : DetailViewAction()
}