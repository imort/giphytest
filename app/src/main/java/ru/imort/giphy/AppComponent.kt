package ru.imort.giphy

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import ru.imort.giphy.api.GiphyApi

/**
 * Service locator based on kotlin props
 * https://blog.kotlin-academy.com/dependency-injection-the-pattern-without-the-framework-33cfa9d5f312
 * Created on 2019-09-28.
 */
interface AppComponent {
    val api: GiphyApi

    companion object {
        lateinit var instance: AppComponent
    }
}

object AppModule : AppComponent {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(loggingInterceptor)
        .build()

    @Suppress("EXPERIMENTAL_API_USAGE")
    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(Json.nonstrict.asConverterFactory("application/json".toMediaType()))
        .baseUrl("https://api.giphy.com/")
        .build()

    override val api: GiphyApi by lazy {
        retrofit.create(GiphyApi::class.java)
    }
}