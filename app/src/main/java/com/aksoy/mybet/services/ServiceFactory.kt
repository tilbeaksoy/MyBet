package com.aksoy.mybet.services

import com.aksoy.mybet.models.response.OddsResponse
import com.aksoy.mybet.models.response.SportsResponse
import retrofit2.Response

class ServiceFactory {

    suspend fun getSports(): Response<ArrayList<SportsResponse>> {
        return ServiceBase.create().getSports()
    }
    suspend fun getSportsOdds(sportKey: String): Response<ArrayList<OddsResponse>> {
        return ServiceBase.create().getSportsOdds(sportKey)
    }

}