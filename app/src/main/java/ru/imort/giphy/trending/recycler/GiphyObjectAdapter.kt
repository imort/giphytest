package ru.imort.giphy.trending.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.imort.giphy.R
import ru.imort.giphy.api.GiphyObject

/**
 * Created on 2019-09-29.
 *
 * @author yaroslav.nastyuk
 */
class GiphyObjectAdapter : ListAdapter<GiphyObject, GiphyObjectViewHolder>(GiphyObject.DiffItemCallback) {
    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int) =
        getItem(position).id.hashCode().toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiphyObjectViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.giphy_object, parent, false)
        return GiphyObjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: GiphyObjectViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}