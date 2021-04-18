package com.axelia.yelpprototype.ui.favorites

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.axelia.yelpprototype.data.repository.BusinessRepository
import com.axelia.yelpprototype.model.Business
import com.axelia.yelpprototype.utils.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
class FavoritesViewModel @ViewModelInject constructor(private val repository: BusinessRepository) :
    ViewModel() {

    lateinit var businessLiveData: LiveData<List<Business>>

    fun getItemFavorites() : LiveData<List<Business>> {
        businessLiveData = repository.getAllItemFavorites().asLiveData()
        return businessLiveData
    }
}
