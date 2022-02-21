package com.aksoy.mybet.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aksoy.mybet.models.response.SportsResponse
import com.aksoy.mybet.services.ServiceFactory
import com.google.gson.Gson
import kotlinx.coroutines.launch

class CategoriesViewModel: ViewModel() {
    val sportsResponse = MutableLiveData<List<SportsResponse>>()
    var apiServices = ServiceFactory()

    fun getSports() {
        viewModelScope.launch {
            val response = apiServices.getSports()
            if (response.isSuccessful) {
                sportsResponse.postValue(response.body())
            }
        }
    }
}