package com.axelia.yelpprototype.ui.details

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import coil.api.load
import com.axelia.yelpprototype.R
import com.axelia.yelpprototype.databinding.ActivityBusinessDetailsBinding
import com.axelia.yelpprototype.model.Business
import com.axelia.yelpprototype.ui.base.BaseActivity
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_business_details.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ItemDetailsActivity : BaseActivity<ItemDetailsViewModel, ActivityBusinessDetailsBinding>() {

    override val mViewModel: ItemDetailsViewModel by viewModels()

    private lateinit var businessItem: Business

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val itemId = intent.extras?.getString(BUSINESS_ID)
            ?: throw IllegalArgumentException("`BUSINESS_ID` must be non-null")

        initItem(itemId)
    }

    private fun initItem(itemId: String) {
        mViewModel.getItem(itemId).observe(this, Observer { item ->
            mViewBinding.contentDetails.apply {
                this@ItemDetailsActivity.businessItem = item

                textviewBusinessName.text = item.name
                textviewBusinessRating.text = item.rating.toString()

                var displayAddress = ""

                item.location.display_address.forEachIndexed { index, element ->
                    val comma = if (index < item.location.display_address.size - 1) ", " else ""
                    displayAddress += element.plus(comma).padEnd(1)
                }

                textviewBusinessAddress.text = displayAddress
                textviewBusinessPhone.text = item.displayPhone

                chipGroup.removeAllViews()
                for (category in item.categories) {
                    val chip = Chip(this@ItemDetailsActivity)
                    chip.text = category.title
                    chip.chipStrokeWidth = dipToPixels(
                            this@ItemDetailsActivity,
                            1f
                    )
                    chip.chipStrokeColor = ColorStateList.valueOf(
                        resources.getColor(android.R.color.darker_gray)
                    )
                    chip.setChipBackgroundColorResource(android.R.color.transparent)
                    chipGroup.addView(chip)
                }

                imageviewIsFavorite.background = getFavoriteIcon(item.isFavorite!!)

                imageviewIsFavorite.setOnClickListener {
                    runBlocking {
                        mViewModel.onFavoriteClicked()
                    }
                }
            }
            mViewBinding.imageviewDetails.load(item.imageUrl)
        })
    }

    private fun getFavoriteIcon(isFavorite : Boolean) : Drawable? {
        return if (isFavorite) {
            ContextCompat.getDrawable(mViewBinding.root.context, R.drawable.ic_baseline_favorite_24)
        } else {
            ContextCompat.getDrawable(mViewBinding.root.context, R.drawable.ic_baseline_favorite_border_24)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun getViewBinding(): ActivityBusinessDetailsBinding =
        ActivityBusinessDetailsBinding.inflate(layoutInflater)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }

            R.id.action_share -> {
                val shareMsg = """
                    ${businessItem.name}
                    Visit: ${businessItem.url}
                """.trimIndent()

                val intent = ShareCompat.IntentBuilder.from(this)
                    .setType("text/plain")
                    .setText(shareMsg)
                    .intent

                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val BUSINESS_ID = "businessId"
    }

    fun dipToPixels(context: Context, dipValue: Float): Float {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics)
    }
}
