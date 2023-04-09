package com.shaderock.lunch.blpmini.feature.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class NetworkViewModel @Inject constructor() : ViewModel() {
    private val _isAuthorizedState: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)
    val isAuthorizedState: LiveData<Boolean> get() = _isAuthorizedState

    fun onUnauthorized() {
        _isAuthorizedState.value = false
    }

    fun onAuthorized() {
        _isAuthorizedState.value = true
    }
}