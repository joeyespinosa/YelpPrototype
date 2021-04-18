package com.axelia.yelpprototype.ui.details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.axelia.yelpprototype.data.repository.BusinessRepository
import com.axelia.yelpprototype.model.Business
import com.axelia.yelpprototype.utils.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
class ItemDetailsViewModel @ViewModelInject constructor(private val repository: BusinessRepository) :
    ViewModel() {

    private lateinit var result: LiveData<Business>

    fun getItem(id: String) : LiveData<Business> {
        result = repository.getItemById(id).asLiveData()
        return result
    }

    fun onFavoriteClicked() = viewModelScope.launch {
        if (result.value!!.isFavorite!!) {
            repository.removeAsFavoriteItem(result.value!!.id)
        } else {
            repository.favoriteItem(result.value!!.id)
        }
    }
}
