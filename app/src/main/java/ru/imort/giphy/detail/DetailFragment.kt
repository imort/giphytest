package ru.imort.giphy.detail

import android.os.Bundle
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.facebook.drawee.backends.pipeline.Fresco
import kotlinx.android.synthetic.main.fragment_detail.*
import ru.imort.giphy.AppComponent
import ru.imort.giphy.R
import ru.imort.giphy.asHumanText
import timber.log.Timber

class DetailFragment : Fragment() {

    private val args: DetailFragmentArgs by navArgs()

    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, VmFactory)
            .get(DetailViewModel::class.java)
        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            render(it)
        })

        errorButton.setOnClickListener {
            load()
        }

        if (savedInstanceState == null) {
            load()
        }
    }

    private fun load() {
        viewModel.dispatch(DetailViewAction.Load(args.id, args.giphy))
    }

    private fun render(state: DetailViewState) {
        Timber.i("render $state")

        val data = state.data
        if (data == null) {
            content.isVisible = false
        } else {
            content.isVisible = true
            image.controller = Fresco.newDraweeControllerBuilder()
                .setUri(data.images.fixed_height.url)
                .setAutoPlayAnimations(true)
                .build()
            val name = data.user?.username
            username.text = if (name.isNullOrBlank()) "Unknown username" else name
            val profileUrl = data.user?.profile_url
            profile.text = if (profileUrl.isNullOrBlank()) "Unknown profile" else profileUrl
            Linkify.addLinks(profile, Linkify.WEB_URLS)
        }

        progress.isVisible = state.loading

        errorLayout.isVisible = state.error != null
        errorText.text = state.error?.asHumanText(requireContext())
    }

    object VmFactory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            if (modelClass.isAssignableFrom(DetailViewModel::class.java))
                DetailViewModel(AppComponent.instance.api) as T
            else throw IllegalArgumentException()
    }
}
