package ru.imort.giphy.trending

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding3.recyclerview.scrollEvents
import com.jakewharton.rxbinding3.swiperefreshlayout.refreshes
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_trending.*
import ru.imort.giphy.AppComponent
import ru.imort.giphy.R
import ru.imort.giphy.asHumanText
import ru.imort.giphy.trending.recycler.GiphyObjectAdapter
import timber.log.Timber

class TrendingFragment : Fragment() {

    private lateinit var viewModel: TrendingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_trending, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler.layoutManager = GridLayoutManager(context, 2)
        recycler.adapter = GiphyObjectAdapter()

        viewModel = ViewModelProviders.of(this, VmFactory)
            .get(TrendingViewModel::class.java)
        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            render(it)
        })

        val refreshes = refreshLayout.refreshes()
            .map {
                TrendingViewAction.Refresh
            }
        val loadNext = recycler.scrollEvents()
            .filter {
                val lm = it.view.layoutManager as LinearLayoutManager
                val totalItemCount = lm.itemCount
                val lastVisibleItem = lm.findLastVisibleItemPosition()
                if (lastVisibleItem + 10 >= totalItemCount) {
                    return@filter true
                }
                return@filter false
            }
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                val currentState = viewModel.viewState.value
                val offset = if (currentState?.nextPageLoading == true) -1 else
                    currentState?.pagination?.next() ?: -1
                TrendingViewAction.LoadForward(offset)
            }
            .filter {
                it.offset != -1
            }
        val errorLoads = errorButton.clicks()
            .map {
                TrendingViewAction.Load
            }
        val actions = Observable.merge(refreshes, loadNext, errorLoads)
        viewModel.bindActions(actions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.unbindActions()
    }

    private var snackbar: Snackbar? = null

    private fun render(state: TrendingViewState) {
        Timber.i("render $state")

        refreshLayout.isVisible = state.firstPageError == null
        refreshLayout.isRefreshing = state.refreshLoading

        val adapter = recycler.adapter as GiphyObjectAdapter
        adapter.submitList(state.data)

        progress.isVisible = state.firstPageLoading

        errorLayout.isVisible = state.firstPageError != null
        errorText.text = state.firstPageError?.asHumanText(requireContext())

        val nextPageError = state.nextPageError
        if (nextPageError == null) {
            snackbar?.dismiss()
        } else {
            snackbar = Snackbar.make(
                requireView(),
                nextPageError.asHumanText(requireContext()),
                Snackbar.LENGTH_INDEFINITE
            )
            snackbar?.show()
        }
    }

    object VmFactory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            if (modelClass.isAssignableFrom(TrendingViewModel::class.java))
                TrendingViewModel(AppComponent.instance.api) as T
            else throw IllegalArgumentException()
    }
}
