package ru.imort.giphy.api

import kotlinx.serialization.Serializable

/**
 * Created on 2019-09-28.
 *
 * @author yaroslav.nastyuk
 */
@Serializable
data class Image(
    val id: String,
    val type: String,
    val title: String,
    val user: User? = null
)

@Serializable
data class User(
    val username: String
)

@Serializable
data class Pagination(
    val offset: Int,
    val count: Int
)

@Serializable
data class Meta(
    val msg: String,
    val status: Int
)

@Serializable
data class TrendingResponse(
    val data: List<Image>,
    val pagination: Pagination,
    val meta: Meta
)

@Serializable
data class GetByIdResponse(
    val data: Image,
    val meta: Meta
)