package ru.imort.giphy

import android.app.Application
import timber.log.Timber

/**
 * Created on 2019-09-28.
 *
 * @author yaroslav.nastyuk
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        AppComponent.instance = AppModule
    }
}