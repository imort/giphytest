package ru.imort.giphy

import android.content.Context
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created on 2019-09-29.
 *
 * @author yaroslav.nastyuk
 */
fun Throwable.asHumanText(
    context: Context, defaultText: CharSequence = context.getString(R.string.error_default)
): CharSequence = when (this) {
    is UnknownHostException, is SocketTimeoutException -> context.getString(R.string.error_no_internet)
    else -> defaultText
}