package com.aksoy.mybet.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aksoy.mybet.models.response.OddsResponse
import com.aksoy.mybet.models.response.SportsResponse
import com.aksoy.mybet.services.ServiceFactory
import com.google.gson.Gson
import kotlinx.coroutines.launch

class CategoriesDetailViewModel : ViewModel() {
    val sportOddsResponse = MutableLiveData<List<OddsResponse>>()
    var apiServices = ServiceFactory()
    fun getSportsOdds(sportKey: String) {
        viewModelScope.launch {
            val response = apiServices.getSportsOdds(sportKey)
            if (response.isSuccessful) {
                sportOddsResponse.postValue(response.body())
            }
        }
    }


}