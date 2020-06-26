package com.axelia.yelpprototype.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.axelia.yelpprototype.databinding.ItemBusinessBinding
import com.axelia.yelpprototype.model.Business
import com.axelia.yelpprototype.ui.main.viewholder.BusinessViewHolder

class BusinessListAdapter(private val onItemClickListener: OnItemClickListener) :
    ListAdapter<Business, BusinessViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BusinessViewHolder(
        ItemBusinessBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: BusinessViewHolder, position: Int) =
        holder.bind(getItem(position), onItemClickListener)

    interface OnItemClickListener {
        fun onItemClicked(item: Business, imageView: ImageView)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Business>() {
            override fun areItemsTheSame(oldItem: Business, newItem: Business): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Business, newItem: Business): Boolean =
                oldItem == newItem

        }
    }
}
