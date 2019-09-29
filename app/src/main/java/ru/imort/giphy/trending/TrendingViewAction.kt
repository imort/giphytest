package ru.imort.giphy.trending

/**
 * Represent Intent in the MVI pattern. Named as such to avoid conflict with android Intent
 * Created on 2019-09-29.
 *
 * @author yaroslav.nastyuk
 */
sealed class TrendingViewAction {
    object Load : TrendingViewAction()
    data class LoadForward(val offset: Int) : TrendingViewAction()
    object Refresh : TrendingViewAction()
}