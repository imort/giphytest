package ru.imort.giphy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_launcher.*
import ru.imort.giphy.detail.DetailFragmentArgs

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        toolbar.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, arguments ->
            when (destination.id) {
                R.id.trendingFragment -> toolbar.title = getString(R.string.trending_title)
                R.id.detailFragment -> {
                    toolbar.title = arguments?.let {
                        DetailFragmentArgs.fromBundle(it).id
                    } ?: getString(R.string.detail_title)
                }
            }
        }
    }
}
