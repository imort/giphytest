package ru.imort.giphy.trending

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_trending.*
import ru.imort.giphy.AppComponent
import ru.imort.giphy.R

class TrendingFragment : Fragment() {

    private lateinit var viewModel: TrendingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trending, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, VmFactory)
            .get(TrendingViewModel::class.java)

        button.setOnClickListener {
            findNavController().navigate(
                TrendingFragmentDirections.actionTrendingFragmentToDetailFragment("whatever")
            )
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
