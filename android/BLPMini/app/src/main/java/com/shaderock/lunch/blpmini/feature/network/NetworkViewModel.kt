package com.shaderock.lunch.blpmini.feature.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class NetworkViewModel @Inject constructor() : ViewModel() {
    private val _isAuthorized: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)
    val isUnauthorized: LiveData<Boolean> get() = _isAuthorized

    fun onUnauthorized() {
        _isAuthorized.value = false
    }

    fun onAuthorized() {
        _isAuthorized.value = true
    }
}