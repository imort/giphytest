package ru.imort.giphy.trending.recycler

import android.view.View
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.giphy_object.*
import ru.imort.giphy.api.GiphyObject
import ru.imort.giphy.trending.TrendingFragmentDirections

/**
 * Created on 2019-09-29.
 *
 * @author yaroslav.nastyuk
 */
class GiphyObjectViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(data: GiphyObject) {
        itemView.setOnClickListener {
            val action = TrendingFragmentDirections.actionTrendingFragmentToDetailFragment(data.id)
            findNavController(it).navigate(action)
        }
        image.setImageURI(data.images.fixed_height_small_still.url)
        text.text = if (data.title.isBlank()) "Unknown title" else data.title
    }
}