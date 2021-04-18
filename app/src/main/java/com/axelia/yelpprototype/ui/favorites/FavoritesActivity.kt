package com.axelia.yelpprototype.ui.favorites

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.axelia.yelpprototype.BuildConfig
import com.axelia.yelpprototype.R
import com.axelia.yelpprototype.databinding.ActivityFavoritesBinding
import com.axelia.yelpprototype.databinding.ActivityMainBinding
import com.axelia.yelpprototype.model.Business
import com.axelia.yelpprototype.ui.base.BaseActivity
import com.axelia.yelpprototype.ui.details.ItemDetailsActivity
import com.axelia.yelpprototype.ui.main.adapter.BusinessListAdapter
import com.axelia.yelpprototype.utils.*
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FavoritesActivity : BaseActivity<FavoritesViewModel, ActivityFavoritesBinding>(),
    BusinessListAdapter.OnItemClickListener {

    override val mViewModel: FavoritesViewModel by viewModels()
    private val mAdapter: BusinessListAdapter by lazy { BusinessListAdapter(onItemClickListener = this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)  // Set AppTheme before setting content view.

        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)

        mViewBinding.recyclerviewBusinessFavorite.apply {
            layoutManager = LinearLayoutManager(this@FavoritesActivity)
            adapter = mAdapter
        }
    }

    override fun onResume() {
        super.onResume()

        initItems()
    }

    private fun initItems() {
        mViewModel.getItemFavorites()
        mViewModel.businessLiveData.observe(this, Observer { data ->
            if (data.isNotEmpty()) {
                mAdapter.submitList(data.toMutableList())
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorites_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_theme) {
            val mode =
                if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                    Configuration.UI_MODE_NIGHT_NO
                ) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                }
            AppCompatDelegate.setDefaultNightMode(mode)
        }

        return true
    }

    override fun getViewBinding(): ActivityFavoritesBinding = ActivityFavoritesBinding.inflate(layoutInflater)

    override fun onItemClicked(item: Business, imageView: ImageView) {
        val intent = Intent(this, ItemDetailsActivity::class.java)
        intent.putExtra(ItemDetailsActivity.BUSINESS_ID, item.id)

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            imageView,
            imageView.transitionName
        )

        startActivity(intent, options.toBundle())
    }
}