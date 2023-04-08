package com.shaderock.lunch.blpmini.feature.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shaderock.lunch.blpmini.domain.usecase.AuthUseCase
import com.shaderock.lunch.blpmini.infrastructure.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {
    private val _authenticationState: MutableLiveData<Resource<Unit>> = MutableLiveData()
    val authenticationState: LiveData<Resource<Unit>> get() = _authenticationState

    private val _authorizationState: MutableLiveData<Resource<Unit>> = MutableLiveData()
    val authorizationState: LiveData<Resource<Unit>> get() = _authorizationState

    init {
        onAuthorize()
    }

    fun onAuthenticate(email: String, password: String) {
        _authenticationState.value = Resource.Loading()
        viewModelScope.launch {
            try {
                authUseCase.login(email, password).collect {
                    _authenticationState.value = Resource.Success(Unit)
                }
            } catch (ex: Exception) {
                _authenticationState.value = Resource.Failure(ex)
                Log.v(null, ex.message ?: "An unknown error occurred during login")
            }
        }
    }

    fun onAuthorize() {
        viewModelScope.launch {
            _authorizationState.value = Resource.Loading()

            try {
                val isAuthorized = authUseCase.isAuthorized().first()
                if (isAuthorized) {
                    _authorizationState.value = Resource.Success(Unit)
                } else {
                    _authorizationState.value = Resource.Failure(UnauthorizedException())
                }
            } catch (ex: Exception) {
                _authorizationState.value = Resource.Failure(ex)
                Log.v(null, ex.message ?: "An unknown error occurred during login")
            }
        }
    }
}

class UnauthorizedException : IOException()
