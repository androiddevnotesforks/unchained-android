package com.github.livingwithhippos.unchained.authentication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.livingwithhippos.unchained.authentication.model.Authentication
import com.github.livingwithhippos.unchained.authentication.model.AuthenticationRepository
import com.github.livingwithhippos.unchained.authentication.model.Secrets
import com.github.livingwithhippos.unchained.base.network.ApiAuthFactory
import kotlinx.coroutines.*

class AuthenticationViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val job = Job()
    val scope = CoroutineScope(Dispatchers.Default + job)

    private val repository: AuthenticationRepository =
        AuthenticationRepository(
            ApiAuthFactory.authApi
        )

    val authLiveData = MutableLiveData<Authentication?>()
    val secretLiveData = MutableLiveData<Secrets?>()

    //todo: here we should check if we already have credentials and if they work, and pass those
    //todo: rename this first part of the auth flow as verificationInfo etc.?
    fun fetchAuthenticationInfo() {
        scope.launch {
            val authData = repository.getVerificationCode()
            authLiveData.postValue(authData)
        }
    }

    fun fetchSecrets(deviceCode: String) {
        //todo: add wait and run every 5 seconds
        val waitTime = 5000L
        scope.launch {
            var secretData = repository.getSecrets(deviceCode)
            secretLiveData.postValue(secretData)
            while (secretData?.clientId == null) {
                delay(waitTime)
                secretData = repository.getSecrets(deviceCode)
            }
            secretLiveData.postValue(secretData)
        }

    }

    fun cancelRequests() = job.cancel()
}