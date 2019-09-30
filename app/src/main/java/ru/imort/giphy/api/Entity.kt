package ru.imort.giphy.api

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

/**
 * Created on 2019-09-28.
 *
 * @author yaroslav.nastyuk
 */
@Serializable
@Parcelize
data class GiphyObject(
    val id: String,
    val type: String,
    val title: String,
    val images: GiphyImages,
    val user: GiphyUser? = null
) : Parcelable {
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
@Parcelize
data class GiphyImages(
    val fixed_height_small_still: GiphyImage,
    val fixed_height: GiphyImage
) : Parcelable

@Serializable
@Parcelize
data class GiphyImage(
    val url: String
) : Parcelable

@Serializable
@Parcelize
data class GiphyUser(
    val username: String,
    val profile_url: String
) : Parcelable

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