package com.axelia.yelpprototype.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.axelia.yelpprototype.R
import com.axelia.yelpprototype.databinding.ActivityMainBinding
import com.axelia.yelpprototype.model.Business
import com.axelia.yelpprototype.ui.base.BaseActivity
import com.axelia.yelpprototype.ui.details.ItemDetailsActivity
import com.axelia.yelpprototype.ui.main.adapter.BusinessListAdapter
import com.axelia.yelpprototype.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(),
    BusinessListAdapter.OnItemClickListener {

    override val mViewModel: MainViewModel by viewModels()
    private val mAdapter: BusinessListAdapter by lazy { BusinessListAdapter(onItemClickListener = this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)  // Set AppTheme before setting content view.

        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)

        mViewBinding.recyclerviewBusiness.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mAdapter
        }

        initItems()
        handleNetworkChanges()
    }

    private fun initItems() {
        mViewModel.businessLiveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> showLoading(true)
                is State.Success -> {
                    if (state.data.isNotEmpty()) {
                        mAdapter.submitList(state.data.toMutableList())
                        showLoading(false)
                    }
                }
                is State.Error -> {
                    showToast(state.message)
                    showLoading(false)
                }
            }
        })

        mViewBinding.buttonSearch.setOnClickListener {
            validate()
        }

        mViewBinding.swiperefreshlayout.setOnRefreshListener {
            validate()
        }
    }

    private fun validate() {
        if (mViewBinding.textinputedittextLocation.textinputedittext_location.text.toString().trim()
                .isEmpty()
        ) {
            mViewBinding.textinputedittextLocation.textinputedittext_location.error = "Location is required";
        } else {
            mViewBinding.textinputedittextLocation.textinputedittext_location.error = null;
            getBusinessItems()
        }
    }

    private fun getBusinessItems() {
        mViewModel.getItems(
            mViewBinding.textinputlayoutTerm.textinputedittext_term.text.toString().trim(),
            mViewBinding.textinputlayoutLocation.textinputedittext_location.text.toString().trim()
        )
    }

    private fun showLoading(isLoading: Boolean) {
        mViewBinding.swiperefreshlayout.isRefreshing = isLoading
    }

    /**
     * Observe network changes i.e. Internet Connectivity
     */
    private fun handleNetworkChanges() {
        NetworkUtils.getNetworkLiveData(applicationContext).observe(this, Observer { isConnected ->
            if (!isConnected) {
                mViewBinding.textviewNetworkStatus.text = getString(R.string.text_no_connectivity)
                mViewBinding.linearlayoutNetworkStatus.apply {
                    show()
                    setBackgroundColor(getColorRes(R.color.colorStatusNotConnected))
                }
            } else {
                mViewBinding.textviewNetworkStatus.text = getString(R.string.text_connectivity)
                mViewBinding.linearlayoutNetworkStatus.apply {
                    setBackgroundColor(getColorRes(R.color.colorStatusConnected))

                    animate()
                        .alpha(1f)
                        .setStartDelay(ANIMATION_DURATION)
                        .setDuration(ANIMATION_DURATION)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                hide()
                            }
                        })
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
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

    override fun onBackPressed() {
        val dialog = MaterialDialog(this).show {
            title(text = getString(R.string.exit_dialog_title))
            message(text = getString(R.string.exit_dialog_message))
            positiveButton(text = getString(R.string.option_yes)) {
                dialog ->
                    dialog.dismiss()
                    super.onBackPressed()
            }
            negativeButton(text = getString(R.string.option_no)) {
                dialog ->
                    dialog.dismiss()
            }
        }

        dialog.show {
            this?.getActionButton(WhichButton.NEGATIVE).updateTextColor(Color.BLACK)
            this?.getActionButton(WhichButton.POSITIVE).updateTextColor(Color.BLACK)
        }
    }

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    companion object {
        const val ANIMATION_DURATION = 1000.toLong()
    }

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
