package com.axelia.yelpprototype.ui.main.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.axelia.yelpprototype.R
import com.axelia.yelpprototype.databinding.ItemBusinessBinding
import com.axelia.yelpprototype.model.Business
import com.axelia.yelpprototype.ui.main.adapter.BusinessListAdapter

class BusinessViewHolder(private val binding: ItemBusinessBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Business, onItemClickListener: BusinessListAdapter.OnItemClickListener? = null) {
        binding.textviewName.text = item.name
        binding.textviewCity.text = item.location.city
        binding.imageviewBusiness.load(item.imageUrl) {
            placeholder(R.drawable.ic_photo)
            error(R.drawable.ic_broken_image)
        }

        onItemClickListener?.let { listener ->
            binding.root.setOnClickListener {
                listener.onItemClicked(item, binding.imageviewBusiness)
            }
        }
    }
}
