package com.axelia.yelpprototype.ui.details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.axelia.yelpprototype.data.repository.BusinessRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class ItemDetailsViewModel @ViewModelInject constructor(private val repository: BusinessRepository) :
    ViewModel() {

    fun getItem(id: String) = repository.getItemById(id).asLiveData()
}
