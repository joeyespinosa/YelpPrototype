package com.axelia.yelpprototype.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axelia.yelpprototype.data.repository.BusinessRepository
import com.axelia.yelpprototype.model.Business
import com.axelia.yelpprototype.utils.State
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * ViewModel for [MainActivity]
 */
@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor(private val repository: BusinessRepository) :
    ViewModel() {

    private val _businessLiveData = MutableLiveData<State<List<Business>>>()

    val businessLiveData: LiveData<State<List<Business>>>
        get() = _businessLiveData


    fun getItems(
        term: String?,
        location: String
    ) {
        viewModelScope.launch {
            repository.getAllItems(
                term, location
            ).collect {
                _businessLiveData.value = it
            }
        }
    }
}
