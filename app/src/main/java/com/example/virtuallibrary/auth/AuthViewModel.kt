package com.example.virtuallibrary.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.virtuallibrary.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository = AuthRepository()) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> get() = _authState

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.signIn(email, password)
            if (result.isSuccess) {
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value =
                    AuthState.Error(result.exceptionOrNull()?.message ?: "Login failed")
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.register(name, email, password)
            if (result.isSuccess) {
                _authState.value = AuthState.Registered
            } else {
                _authState.value =
                    AuthState.Error(result.exceptionOrNull()?.message ?: "Registration failed")
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _authState.value = AuthState.LoggedOut
        }
    }
}

sealed class AuthState {
    object Authenticated : AuthState()
    object Registered : AuthState()
    object LoggedOut : AuthState()
    data class Error(val message: String) : AuthState()
}

