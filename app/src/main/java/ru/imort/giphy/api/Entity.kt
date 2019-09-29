package ru.imort.giphy.api

import androidx.recyclerview.widget.DiffUtil
import kotlinx.serialization.Serializable

/**
 * Created on 2019-09-28.
 *
 * @author yaroslav.nastyuk
 */
@Serializable
data class GiphyObject(
    val id: String,
    val type: String,
    val title: String,
    val images: GiphyImages,
    val user: GiphyUser? = null
) {
    object DiffItemCallback : DiffUtil.ItemCallback<GiphyObject>() {
        override fun areItemsTheSame(oldItem: GiphyObject, newItem: GiphyObject): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GiphyObject, newItem: GiphyObject): Boolean {
            return oldItem == newItem
        }
    }
}

@Serializable
data class GiphyImages(
    val fixed_height_small_still: GiphyImage,
    val fixed_height: GiphyImage
)

@Serializable
data class GiphyImage(
    val url: String
)

@Serializable
data class GiphyUser(
    val username: String
)

@Serializable
data class GiphyPagination(
    val offset: Int,
    val count: Int
) {
    fun next() = offset + count
}

@Serializable
data class GiphyMeta(
    val msg: String,
    val status: Int
)

@Serializable
data class TrendingResponse(
    val data: List<GiphyObject>,
    val pagination: GiphyPagination,
    val meta: GiphyMeta
)

@Serializable
data class GetByIdResponse(
    val data: GiphyObject,
    val meta: GiphyMeta
)