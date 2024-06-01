package com.example.virtuallibrary.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.virtuallibrary.data.AuthRepository

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _registerStatus = MutableLiveData<Pair<Boolean, String?>>()
    val registerStatus: LiveData<Pair<Boolean, String?>> get() = _registerStatus

    private val _loginStatus = MutableLiveData<Pair<Boolean, String?>>()
    val loginStatus: LiveData<Pair<Boolean, String?>> get() = _loginStatus

    fun registerUser(name: String, email: String, password: String) {
        repository.registerUser(name, email, password) { success, message ->
            _registerStatus.value = Pair(success, message)
        }
    }

    fun loginUser(email: String, password: String) {
        repository.loginUser(email, password) { success, message ->
            _loginStatus.value = Pair(success, message)
        }
    }
}