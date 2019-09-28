package ru.imort.giphy.api

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created on 2019-09-28.
 *
 * @author yaroslav.nastyuk
 */
interface GiphyApi {
    @GET("v1/gifs/trending")
    fun trending(
        @Query("api_key") key: String = "CHctsKmVO116OvRpIXvYpUpfyUGmLliU",
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): Single<TrendingResponse>
}